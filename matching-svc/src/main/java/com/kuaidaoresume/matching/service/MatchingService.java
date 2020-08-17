package com.kuaidaoresume.matching.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auditlog.LogEntry;
import com.kuaidaoresume.common.auth.AuthContext;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.matching.dto.MatchingDto;
import com.kuaidaoresume.matching.dto.MatchingList;
import com.kuaidaoresume.matching.model.Matching;
import com.kuaidaoresume.matching.repo.MatchingRepo;
import com.kuaidaoresume.matching.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MatchingService {

    static ILogger logger = SLoggerFactory.getLogger(MatchingService.class);

    @Autowired
    private final MatchingRepo matchingRepo;

    @Autowired
    private final EnvConfig envConfig;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ServiceHelper serviceHelper;

    @PersistenceContext
    private EntityManager entityManager;

    public MatchingDto createMatching(MatchingDto matchingDto) {
        Matching matching = convertToModel(matchingDto);

        Matching savedMatching = null;
        try {
            savedMatching = matchingRepo.save(matching);
        } catch (Exception ex) {
            String errMsg = "could not create matching";
            serviceHelper.handleErrorAndThrowException(logger, ex, errMsg);
        }

        LogEntry auditLog = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("matching")
                //.targetId(matching.getId()) TODO: Aaron Liu after repo donw.
                //.matchingId(matching.getId())
                .updatedContents(matching.toString())
                .build();
        logger.info("created matching", auditLog);
        serviceHelper.trackEventAsync("matching_created");
        return convertToDto(savedMatching);
    }

    public MatchingDto getMatching(String matchingId) {
        Optional<Matching> matching = matchingRepo.findById(Long.parseLong(matchingId));
        if (!matching.isPresent()) {
            throw new ServiceException(ResultCode.NOT_FOUND, "Matching Job not found");
        }
        return convertToDto(matching.get());
    }

    public MatchingList listMatchings(int offset, int limit, String location, String major, String[] keywords) {
        Pageable pageRequest = PageRequest.of(offset, limit);
        Page<Matching> matchingPage = null;
        try {
            matchingPage = matchingRepo.findAll(pageRequest);
            //TODO: After Matching repo setup from TAIL-130 by fetching localtion, major and keywords
        } catch (Exception ex) {
            String errMsg = "fail to query database for matching list";
            serviceHelper.handleErrorAndThrowException(logger, ex, errMsg);
        }
        List<MatchingDto> matchingDtoList = matchingPage.getContent()
                .stream()
                .map(matching -> convertToDto(matching))
                .collect(toList());

        return MatchingList.builder()
            .limit(limit)
            .offset(offset)
            .matchings(matchingDtoList)
            .build();
    }

    public MatchingDto getMatchingByResumeId(String resumeId) {
        Optional<Matching> matching = Optional.empty();
        //TODO: After Matching repo setup from TAIL-130
        if (!matching.isPresent()) {
            throw new ServiceException(ResultCode.NOT_FOUND, "Matching Job not found");
        }
        return convertToDto(matching.get());
    }

    public MatchingDto updateMatching(MatchingDto matchingDto) {
        Optional<Matching> existingMatching = matchingRepo.findById(Long.parseLong(matchingDto.getId()));
        if (!existingMatching.isPresent()) {
            throw new ServiceException(ResultCode.NOT_FOUND, "Matching not found");
        }
        entityManager.detach(existingMatching);

        Matching matchingToUpdate = convertToModel(matchingDto);
        Matching updatedMatching = null;
        try {
            updatedMatching = matchingRepo.save(matchingToUpdate);
        } catch (Exception ex) {
            String errMsg = "could not update the matchingDto";
            serviceHelper.handleErrorAndThrowException(logger, ex, errMsg);
        }

        LogEntry auditLog = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("matching")
                //.targetId(matchingToUpdate.getId())
                //.matchingId(matchingToUpdate.getId())
                .originalContents(existingMatching.toString())
                .updatedContents(updatedMatching.toString())
                .build();
        logger.info("updated matching", auditLog);
        serviceHelper.trackEventAsync("matching_updated");
        return this.convertToDto(updatedMatching);
    }

    public MatchingDto updateMatching(String matchingId, String resumeId, String userId) {
        Optional<Matching> existingMatching = matchingRepo.findById(Long.parseLong(matchingId));
        if (!existingMatching.isPresent()) {
            throw new ServiceException(ResultCode.NOT_FOUND, "Matching not found");
        }
        Matching updatedMatching = null;
        // TODO: After Matching repo setup from TAIL-130
        // existingg matching query update.
        try {
            updatedMatching = matchingRepo.save(existingMatching.get());
        } catch (Exception ex) {
            String errMsg = "could not update the matchingDto";
            serviceHelper.handleErrorAndThrowException(logger, ex, errMsg);
        }
        LogEntry auditLog = LogEntry.builder()
                .currentUserId(userId)
                .authorization(AuthContext.getAuthz())
                .targetType("matching")
                //.targetId(matchingToUpdate.getId())
                //.matchingId(matchingToUpdate.getId())
                .originalContents(existingMatching.toString())
                .updatedContents(updatedMatching.toString())
                .build();
        logger.info("updated matching", auditLog);
        serviceHelper.trackEventAsync("matching_updated");

        return this.convertToDto(updatedMatching); // here should be matching-resume DTO?
    }

    public String inactiveMatching(String matchingId) {
        Optional<Matching> existingMatching = matchingRepo.findById(Long.parseLong(matchingId));
        if (!existingMatching.isPresent()) {
            throw new ServiceException(ResultCode.NOT_FOUND, "Matching not found");
        }
        Matching updatedMatching = null;
        // TODO: After Matching repo setup from TAIL-130
        // existingg matching query update.
        try {
            updatedMatching = matchingRepo.save(existingMatching.get());
        } catch (Exception ex) {
            String errMsg = "could not update the matchingDto";
            serviceHelper.handleErrorAndThrowException(logger, ex, errMsg);
        }
        LogEntry auditLog = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("matching")
                //.targetId(matchingToUpdate.getId())
                //.matchingId(matchingToUpdate.getId())
                .originalContents(existingMatching.toString())
                .updatedContents(updatedMatching.toString())
                .build();
        logger.info("inactive matching", auditLog);
        serviceHelper.trackEventAsync("matching_inactive");

        return updatedMatching.getId();
    }

    private Matching convertToModel(MatchingDto matchingDto) {
        return modelMapper.map(matchingDto, Matching.class);
    }

    private MatchingDto convertToDto(Matching matching) {
        return modelMapper.map(matching, MatchingDto.class);
    }
}
