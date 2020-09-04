package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ResumeContainableRepositoryFactory {

    @Autowired
    private BasicInfoRepository basicInfoRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private ProjectExperienceRepository projectExperienceRepository;

    @Autowired
    private VolunteerExperienceRepository volunteerExperienceRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    public ResumeContainableRepository getResumeContainableRepository(Class<? extends ResumeContainable> type) {
        if (type.equals(BasicInfo.class)) {
            return basicInfoRepository;
        } else if (type.equals(Education.class)) {
            return educationRepository;
        } else if (type.equals(WorkExperience.class)) {
            return workExperienceRepository;
        } else if (type.equals(ProjectExperience.class)) {
            return projectExperienceRepository;
        } else if (type.equals(VolunteerExperience.class)) {
            return volunteerExperienceRepository;
        } else if (type.equals(Certificate.class)) {
            return certificateRepository;
        } else {
            throw new IllegalArgumentException("Invalid class type");
        }
    }
}
