package com.kuaidaoresume.job.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.auditlog.LogEntry;
import com.kuaidaoresume.common.auth.AuthContext;
import com.kuaidaoresume.common.error.ResourceNotFoundException;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.job.dto.SuggestionDto;
import com.kuaidaoresume.job.dto.SuggestionListDto;
import com.kuaidaoresume.job.model.Suggestion;
import com.kuaidaoresume.job.repository.SuggestionRepository;
import com.kuaidaoresume.job.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    static ILogger logger = SLoggerFactory.getLogger(SuggestionServiceImpl.class);

    private final SuggestionRepository suggestionRepository;

    private final ModelMapper modelMapper;

    private final ServiceHelper serviceHelper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SuggestionDto getSuggestionById(Long id) {
        Suggestion suggestion = suggestionRepository.findSuggestionById(id);
        if(suggestion == null) {
            throw new ServiceException(String.format("User with id %s not found", id));
        }
        return modelMapper.map(suggestion, SuggestionDto.class);
    }

    @Override
    public SuggestionListDto getAListByIndustryAndPosition(String industry, String position, int offset, int limit) {
        Pageable pageRequest = PageRequest.of(offset, limit);
        Page<Suggestion> suggestionPage = suggestionRepository.findSuggestionByIndustryAndAndPositionTitle(industry, position, pageRequest);
        List<SuggestionDto> suggestionDtoList = suggestionPage.getContent()
                .stream()
                .map(suggestion -> modelMapper.map(suggestion, SuggestionDto.class))
                .collect(toList());
        //TODO Aaron Liu. Add In-memory cache for pageable.
        return SuggestionListDto.builder()
                .limit(limit)
                .offset(offset)
                .suggestions(suggestionDtoList)
                .build();
    }

    @Override
    public SuggestionDto addSuggestion(String industry, String position, String texts, String keywords) {
        Suggestion suggestion = Suggestion.builder()
                .industry(industry)
                .positionTitle(position)
                .texts(texts)
                .suggestionKeywords(keywords)
                .build();
        try {
            suggestionRepository.save(suggestion);
        } catch (Exception ex) {
            String errMsg = "Could not add the job suggestion";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("suggestion")
                .targetId(String.valueOf(suggestion.getId()))
                .updatedContents(suggestion.toString())
                .build();
        logger.info("created job suggestion", auditLog);
        return modelMapper.map(suggestion, SuggestionDto.class);
    }

    @Override
    @Transactional
    public SuggestionDto updateSuggestion(SuggestionDto aSuggestion) {
        Suggestion newSuggestion = modelMapper.map(aSuggestion, Suggestion.class);

        Suggestion existingSuggestion = suggestionRepository
                .findSuggestionById(newSuggestion.getId());
        if (existingSuggestion == null) {
            throw new ResourceNotFoundException(String.format("Suggestion with id %s not found", newSuggestion.getId()));
        }
        entityManager.detach(existingSuggestion);

        try {
            suggestionRepository.save(newSuggestion);
        } catch (Exception ex) {
            String errMsg = "Could not update the job suggestion";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("suggestion")
                .targetId(String.valueOf(newSuggestion.getId()))
                .originalContents(existingSuggestion.toString())
                .updatedContents(newSuggestion.toString())
                .build();
        logger.info("created job suggestion", auditLog);
        return modelMapper.map(newSuggestion, SuggestionDto.class);
    }

    @Override
    public void deleteASuggestion(Long suggestionID) {
        try {
            suggestionRepository.deleteById(suggestionID);
        } catch (Exception ex) {
            String errMsg = "failed to delete suggestion";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
        LogEntry auditLog = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("suggestion")
                .targetId(String.valueOf(suggestionID))
                .build();

        logger.info("deleted suggestion", auditLog);
    }
}
