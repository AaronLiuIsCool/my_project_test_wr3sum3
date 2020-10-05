package com.kuaidaoresume.job.controller.v1;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SuggestionController {

    static final ILogger logger = SLoggerFactory.getLogger(SuggestionController.class);

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private final ModelMapper modelMapper;

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/suggestion/{id}")
    public SuggestionGenericResponse getSuggestionById(@PathVariable long id) {
        return new SuggestionGenericResponse(suggestionService.getSuggestionById(id));
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_WHOAMI_SERVICE,
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @GetMapping("/get_suggestions_by_industry_position")
    public SuggestionListDto getAListByIndustryAndPosition(
            @RequestParam String industry,
            @RequestParam String position,
            @RequestParam int offset,
            @RequestParam @Min(0) int limit) {
        SuggestionListDto suggestionListDto = suggestionService.getAListByIndustryAndPosition(industry, position, offset, limit);
        return suggestionListDto;
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/suggestion")
    public SuggestionGenericResponse addSuggestion(@RequestBody @Valid SuggestionCreateRequest req) {
        SuggestionDto suggestionDto = suggestionService.addSuggestion(
                req.getIndustry(), req.getPositionTitle(), req.getTexts(), req.getSuggestionKeywords());
        return new SuggestionGenericResponse(suggestionDto);
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PutMapping("/suggestion/{id}")
    public SuggestionGenericResponse updateSuggestion(@PathVariable long id, @RequestBody @Valid SuggestionDto newSuggestionDto) {
        SuggestionDto suggestionDto = suggestionService.updateSuggestion(newSuggestionDto);
        return new SuggestionGenericResponse(suggestionDto);
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @DeleteMapping("/suggestion/{id}")
    public BaseResponse deleteASuggestion(@PathVariable long id) {
        suggestionService.deleteASuggestion(id);
        return BaseResponse.builder().message("Suggestion deleted").build();
    }
}