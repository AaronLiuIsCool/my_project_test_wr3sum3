package com.kuaidaoresume.job.controller.v1;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Authorize;
import com.kuaidaoresume.job.service.TailorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.kuaidaoresume.job.dto.*;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/jobs/tailor")
@RequiredArgsConstructor
public class TailorController {

    static final ILogger logger = SLoggerFactory.getLogger(TailorController.class);

    @Autowired
    private TailorService tailorService;

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/education")
    public TailorResponse education(
            @RequestBody
            @NotEmpty(message = "list educations cannot be empty.")
                    List<@Valid EducationDto> educationDtos) {
        return new TailorResponse(tailorService.education(educationDtos));
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/certificate")
    public TailorResponse certificate(
            @RequestBody
            @NotEmpty(message = "list certificates cannot be empty.")
                    List<@Valid CertificateDto> certificateDtos) {
        return new TailorResponse(tailorService.certificate(certificateDtos));
    }

    @Authorize(value = {
            AuthConstant.AUTHORIZATION_WWW_SERVICE,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_SUPERPOWERS_SERVICE
    })
    @PostMapping("/experience")
    public TailorResponse experience(
            @RequestBody
            @NotEmpty(message = "list work experiences cannot be empty.")
                    List<@Valid ExperienceDto> experienceDtos) {
        return new TailorResponse(tailorService.experience(experienceDtos));
    }
}
