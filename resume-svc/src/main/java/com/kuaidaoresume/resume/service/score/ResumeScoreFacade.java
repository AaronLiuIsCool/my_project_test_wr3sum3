package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.dto.*;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.model.ProjectExperience;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.model.WorkExperience;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ResumeScoreFacade {

    @Autowired
    private final CompletenessScoreCalculator completenessScoreCalculator;
    @Autowired
    private final PhotoScoreCalculator photoScoreCalculator;
    @Autowired
    private final ExperienceKeywordScoreCalculator experienceKeywordScoreCalculator;
    @Autowired
    private final ExperienceNumericWordScoreCalculator experienceNumericWordScoreCalculator;
    @Autowired
    private final UniversityRankScoreCalculator universityRankScoreCalculator;
    @Autowired
    private final GpaScoreCalculator gpaScoreCalculator;
    @Autowired
    private final CompanyRankScoreCalculator companyRankScoreCalculator;
    @Autowired
    private final WorkExperiencesScoreCalculator workExperiencesScoreCalculator;
    @Autowired
    private final WorkExperienceDurationScoreCalculator workExperienceDurationScoreCalculator;
    @Autowired
    private final ProjectExperiencesScoreCalculator projectExperiencesScoreCalculator;
    @Autowired
    private final ProjectExperienceDurationScoreCalculator projectExperienceDurationScoreCalculator;
    @Autowired
    private final CertificatesScoreCalculator certificatesScoreCalculator;

    public ResumeScoreDto getResumeScore(Resume resume) {
        Collection<Education> educations = resume.getEducations();
        Collection<WorkExperience> workExperiences = resume.getWorkExperiences();
        Collection<ProjectExperience> projectExperiences = resume.getProjectExperiences();

        return ResumeScoreDto.builder()
            .resumeId(resume.getId())
            .completenessScore(completenessScoreCalculator.calculateScore(resume))
            .photoScore(photoScoreCalculator.calculateScore(resume))
            .experiencesKeywordScore(experienceKeywordScoreCalculator.calculateScore(resume))
            .experiencesNumericWordScore(experienceNumericWordScoreCalculator.calculateScore(resume))
            .universityRankScores(getUniversityRankScores(educations))
            .gpaScores(getGpaScores(educations))
            .companyRankScores(getCompanyRankScores(workExperiences))
            .workExperiencesScore(workExperiencesScoreCalculator.calculateScore(resume))
            .workExperienceDurationScores(getWorkExperienceDurationScores(workExperiences))
            .projectExperiencesScore(projectExperiencesScoreCalculator.calculateScore(resume))
            .projectExperienceDurationScores(getProjectExperienceDurationScores(projectExperiences))
            .certificatesScore(certificatesScoreCalculator.calculateScore(resume))
            .totalScore(this.getTotalScore(resume))
            .build();
    }

    private Collection<UniversityRankScoreDto> getUniversityRankScores(Collection<Education> educations) {
        return educations.stream()
            .map(education -> UniversityRankScoreDto.builder()
                .educationId(education.getId())
                .score(universityRankScoreCalculator.calculateScore(education))
                .build()
            ).collect(Collectors.toList());
    }

    private Collection<GpaScoreDto> getGpaScores(Collection<Education> educations) {
        return educations.stream()
            .map(education -> GpaScoreDto.builder()
                .educationId(education.getId())
                .score(gpaScoreCalculator.calculateScore(education))
                .build()
            ).collect(Collectors.toList());
    }

    private Collection<CompanyRankScoreDto> getCompanyRankScores(Collection<WorkExperience> workExperiences) {
        return workExperiences.stream()
            .map(workExperience -> CompanyRankScoreDto.builder()
                .workExperienceId(workExperience.getId())
                .score(companyRankScoreCalculator.calculateScore(workExperience))
                .build()
            ).collect(Collectors.toList());
    }

    private Collection<WorkExperienceDurationScoreDto> getWorkExperienceDurationScores(Collection<WorkExperience> workExperiences) {
        return workExperiences.stream()
            .map(workExperience -> WorkExperienceDurationScoreDto.builder()
                .workExperienceId(workExperience.getId())
                .score(workExperienceDurationScoreCalculator.calculateScore(workExperience))
                .build()
            ).collect(Collectors.toList());
    }

    private Collection<ProjectExperienceDurationScoreDto> getProjectExperienceDurationScores(Collection<ProjectExperience> projectExperiences) {
        return projectExperiences.stream()
            .map(projectExperience -> ProjectExperienceDurationScoreDto.builder()
                .projectExperienceId(projectExperience.getId())
                .score(projectExperienceDurationScoreCalculator.calculateScore(projectExperience))
                .build())
            .collect(Collectors.toList());
    }

    public int getTotalScore(Resume resume) {
        Collection<Education> educations = resume.getEducations();
        Collection<WorkExperience> workExperiences = resume.getWorkExperiences();

        int totalScore = 0;
        totalScore += completenessScoreCalculator.calculateScore(resume);
        totalScore += photoScoreCalculator.calculateScore(resume);
        totalScore += experienceKeywordScoreCalculator.calculateScore(resume);
        totalScore += experienceNumericWordScoreCalculator.calculateScore(resume);
        totalScore += educations.stream()
            .map(universityRankScoreCalculator::calculateScore)
            .reduce(0, (acc, cur) -> acc + cur);
        totalScore += educations.stream()
            .map(gpaScoreCalculator::calculateScore)
            .reduce(0, (acc, cur) -> acc + cur);
        totalScore += workExperiences.stream()
            .map(companyRankScoreCalculator::calculateScore)
            .reduce(0, (acc, cur) -> acc + cur);
        totalScore += workExperiencesScoreCalculator.calculateScore(resume);
        totalScore += workExperiences.stream()
            .map(workExperienceDurationScoreCalculator::calculateScore)
            .reduce(0, (acc, cur) -> acc + cur);
        totalScore += projectExperiencesScoreCalculator.calculateScore(resume);
        totalScore += resume.getProjectExperiences().stream()
            .map(projectExperienceDurationScoreCalculator::calculateScore)
            .reduce(0, (acc, cur) -> acc + cur);
        totalScore += certificatesScoreCalculator.calculateScore(resume);

        return totalScore;
    }
}
