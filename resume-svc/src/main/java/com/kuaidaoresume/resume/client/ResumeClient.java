package com.kuaidaoresume.resume.client;

import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.resume.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.kuaidaoresume.common.auth.AuthConstant.AUTHORIZATION_RESUME_SERVICE;

@FeignClient(name = AUTHORIZATION_RESUME_SERVICE, path = "/v1", url = "${kuaidaoresume.resume-service-endpoint}")
public interface ResumeClient {

    @GetMapping("/resumes/{id}")
    ResponseEntity<EntityModel<PersistedResumeDto>> findResumeById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String id);

    @GetMapping("/resumes/{id}/score")
    ResponseEntity<EntityModel<ResumeScoreDto>> getResumeScore(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String id);

    @PostMapping("/resumes")
    ResponseEntity<EntityModel<PersistedResumeDto>> createResume(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @Valid @RequestBody ResumeDto resumeDto);

    @GetMapping("/basic-infos/{id}")
    ResponseEntity<EntityModel<PersistedBasicInfoDto>> findBasicInfoById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,@PathVariable Long id);

    @GetMapping("/resumes/{resumeId}/basic-info")
    ResponseEntity<EntityModel<PersistedBasicInfoDto>> findBasicInfoByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId);

    @PostMapping("/resumes/{resumeId}/basic-info")
    ResponseEntity<EntityModel<PersistedBasicInfoDto>> createBasicInfo(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody BasicInfoDto basicInfoDto);

    @PutMapping("/resumes/{resumeId}/basic-info")
    ResponseEntity<EntityModel<PersistedBasicInfoDto>> saveBasicInfo(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody PersistedBasicInfoDto basicInfoDto);
    
    @GetMapping("/certificates/{id}")
    ResponseEntity<EntityModel<PersistedCertificateDto>> findCertificateById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @GetMapping("/resumes/{resumeId}/certificates")
    ResponseEntity<CollectionModel<EntityModel<PersistedCertificateDto>>> findAllCertificatesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @PostMapping("/resumes/{resumeId}/certificates")
    ResponseEntity<EntityModel<PersistedCertificateDto>> createCertificate(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody CertificateDto certificateDto);

    @PutMapping("/certificates/{id}")
    ResponseEntity<EntityModel<PersistedCertificateDto>> saveCertificate(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable Long id,
        @Valid @RequestBody PersistedCertificateDto certificateDto);

    @PutMapping("/resumes/{resumeId}/certificates")
    ResponseEntity<CollectionModel<EntityModel<PersistedCertificateDto>>> saveCertificates(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedCertificateDto> certificateDtosBatch);

    @DeleteMapping("/certificates/{id}")
    ResponseEntity<?> deleteCertificate(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @DeleteMapping("/resumes/{resumeId}/certificates")
    ResponseEntity<?> deleteCertificatesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @GetMapping("/educations/{id}")
    ResponseEntity<EntityModel<PersistedEducationDto>> findEducationById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @GetMapping("/resumes/{resumeId}/educations")
   ResponseEntity<CollectionModel<EntityModel<PersistedEducationDto>>> findEducationsByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @PostMapping("/resumes/{resumeId}/educations")
    ResponseEntity<EntityModel<PersistedEducationDto>> createEducation(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody EducationDto educationDto);

    @PutMapping("/educations/{id}")
    ResponseEntity<EntityModel<PersistedEducationDto>> saveEducation(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable Long id,
        @Valid @RequestBody PersistedEducationDto educationDto);

    @PutMapping("/resumes/{resumeId}/educations")
    ResponseEntity<CollectionModel<EntityModel<PersistedEducationDto>>> saveEducations(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedEducationDto> educationDtoBatch);

    @DeleteMapping("/educations/{id}")
    ResponseEntity<?> deleteEducationById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @DeleteMapping("/resumes/{resumeId}/educations")
    ResponseEntity<?> deleteEducationsByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @PostMapping("/keywords")
    ResponseEntity<?> addKeywords(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @RequestBody Collection<String> keywords);

    @GetMapping("/project-experiences/{id}")
    ResponseEntity<EntityModel<PersistedProjectExperienceDto>> findProjectExperienceById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @GetMapping("/resumes/{resumeId}/project-experiences")
    ResponseEntity<CollectionModel<EntityModel<PersistedProjectExperienceDto>>> findProjectExperiencesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @PostMapping("/resumes/{resumeId}/project-experiences")
    ResponseEntity<EntityModel<PersistedProjectExperienceDto>> createProjectExperience(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody ExperienceDto experienceDto);

    @PutMapping("/project-experiences/{id}")
    ResponseEntity<EntityModel<PersistedProjectExperienceDto>> saveProjectExperience(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable Long id,
        @Valid @RequestBody PersistedProjectExperienceDto experienceDto);

    @PutMapping("/resumes/{resumeId}/project-experiences")
    ResponseEntity<CollectionModel<EntityModel<PersistedProjectExperienceDto>>> saveProjectExperiences(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedProjectExperienceDto> experienceDtosBatch);

    @DeleteMapping("/project-experiences/{id}")
    ResponseEntity<?> deleteProjectExperienceById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @DeleteMapping("/resumes/{resumeId}/project-experiences")
    ResponseEntity<?> deleteProjectExperiencesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @GetMapping("/volunteer-experiences/{id}")
    ResponseEntity<EntityModel<PersistedVolunteerExperienceDto>> findVolunteerExperienceById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @GetMapping("/resumes/{resumeId}/volunteer-experiences")
    ResponseEntity<CollectionModel<EntityModel<PersistedVolunteerExperienceDto>>> findVolunteerExperiencesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @PostMapping("/resumes/{resumeId}/volunteer-experiences")
    ResponseEntity<EntityModel<PersistedVolunteerExperienceDto>> createVolunteer(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody ExperienceDto experienceDto);

    @PutMapping("/volunteer-experiences/{id}")
    ResponseEntity<EntityModel<PersistedVolunteerExperienceDto>> saveVolunteerExperience(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable Long id,
        @Valid @RequestBody PersistedVolunteerExperienceDto experienceDto);

    @PutMapping("/resumes/{resumeId}/volunteer-experiences")
    ResponseEntity<CollectionModel<EntityModel<PersistedVolunteerExperienceDto>>> saveVolunteerExperiences(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedVolunteerExperienceDto> experienceDtosBatch);

    @DeleteMapping("/volunteer-experiences/{id}")
    ResponseEntity<?> deleteVolunteerExperienceById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @DeleteMapping("/resumes/{resumeId}/volunteer-experiences")
    ResponseEntity<?> deleteVolunteerExperiencesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @GetMapping("/work-experiences/{id}")
    ResponseEntity<EntityModel<PersistedWorkExperienceDto>> findWorkExperienceById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @GetMapping("/resumes/{resumeId}/work-experiences")
    ResponseEntity<CollectionModel<EntityModel<PersistedWorkExperienceDto>>> findWorkExperiencesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);

    @PostMapping("/resumes/{resumeId}/work-experiences")
    ResponseEntity<EntityModel<PersistedWorkExperienceDto>> createWorkExperience(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody ExperienceDto experienceDto);

    @PutMapping("/work-experiences/{id}")
    ResponseEntity<EntityModel<PersistedWorkExperienceDto>> saveWorkExperience(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable Long id,
        @Valid @RequestBody PersistedWorkExperienceDto experienceDto);

    @PutMapping("/resumes/{resumeId}/work-experiences")
    ResponseEntity<CollectionModel<EntityModel<PersistedWorkExperienceDto>>> saveWorkExperiences(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth,
        @PathVariable String resumeId,
        @Valid @RequestBody List<PersistedWorkExperienceDto> experienceDtosBatch);

    @DeleteMapping("/work-experiences/{id}")
    ResponseEntity<?> deleteWorkExperienceById(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable Long id);

    @DeleteMapping("/resumes/{resumeId}/work-experiences")
    ResponseEntity<?> deleteWorkExperiencesByResumeId(
        @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String auth, @PathVariable String resumeId);
}
