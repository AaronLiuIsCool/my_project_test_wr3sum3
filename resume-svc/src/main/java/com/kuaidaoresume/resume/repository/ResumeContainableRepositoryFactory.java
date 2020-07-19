package com.kuaidaoresume.resume.repository;

import com.kuaidaoresume.resume.model.BasicInfo;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.model.ResumeContainable;
import com.kuaidaoresume.resume.model.WorkExperience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ResumeContainableRepositoryFactory {

    @Autowired
    private BasicInfoRepository basicInfoRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    private Map<Class<? extends ResumeContainable>, ResumeContainableRepository> repositoriesMap;

    public ResumeContainableRepositoryFactory() {
        repositoriesMap = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(BasicInfo.class, basicInfoRepository),
            new AbstractMap.SimpleImmutableEntry<>(Education.class, educationRepository),
            new AbstractMap.SimpleImmutableEntry<>(WorkExperience.class, workExperienceRepository))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public ResumeContainableRepository getResumeContainableRepository(Class<? extends ResumeContainable> type) {
        return repositoriesMap.get(type);
    }
}
