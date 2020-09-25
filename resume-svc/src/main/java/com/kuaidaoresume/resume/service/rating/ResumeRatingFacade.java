package com.kuaidaoresume.resume.service.rating;

import com.kuaidaoresume.resume.dto.EducationRatingDto;
import com.kuaidaoresume.resume.dto.ExperienceRatingDto;
import com.kuaidaoresume.resume.dto.ResumeRatingDto;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.model.Experience;
import com.kuaidaoresume.resume.model.Resume;
import com.kuaidaoresume.resume.service.score.ResumeScoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ResumeRatingFacade {

    @Autowired
    private final ResumeScoreFacade resumeScoreFacade;
    @Autowired
    private final EducationRatingBuilder educationRatingBuilder;
    @Autowired
    private final ExperienceRatingBuilder experienceRatingBuilder;

    public ResumeRatingDto getResumeRating(Resume resume) {
        return ResumeRatingDto.builder()
            .resumeUuid(resume.getId())
            .score(getScore(resume))
            .educations(getEducationRatings(resume.getEducations()))
            .workExperiences(getExperienceRatings(resume.getWorkExperiences()))
            .projectExperiences(getExperienceRatings(resume.getProjectExperiences()))
            .volunteerExperiences(getExperienceRatings(resume.getVolunteerExperiences()))
            .numCertificates(resume.getCertificates().size())
            .build();
    }

    private int getScore(Resume resume) {
        return resumeScoreFacade.getTotalScore(resume);
    }

    private Collection<EducationRatingDto> getEducationRatings(Collection<Education> educations) {
        return educations.stream()
            .map(educationRatingBuilder::getEducationRating)
            .collect(Collectors.toList());
    }

    private <T extends Experience> Collection<ExperienceRatingDto> getExperienceRatings(Collection<T> experiences) {
        return experiences.stream()
            .map(experienceRatingBuilder::getExperienceRating)
            .collect(Collectors.toList());
    }
}
