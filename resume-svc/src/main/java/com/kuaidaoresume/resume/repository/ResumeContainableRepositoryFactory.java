package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.model.ResumeContainable;
import com.kuaidaoresume.resume.model.WorkExperience;
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

    public ResumeContainableRepository getResumeContainableRepository(Class<? extends ResumeContainable> type) {
        if (type.equals(BasicInfo.class)) {
            return basicInfoRepository;
        } else if (type.equals(Education.class)) {
            return educationRepository;
        } else if (type.equals(WorkExperience.class)) {
            return workExperienceRepository;
        } else {
            throw new IllegalArgumentException("Invalid class type");
        }
    }
}
