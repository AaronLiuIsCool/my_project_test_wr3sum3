package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.ContainsMatcher;
import com.kuaidaoresume.resume.model.*;
import com.kuaidaoresume.resume.utils.ResumeDescriptionUtil;

import java.util.Collection;

public abstract class AbstractExperienceStrengthScoreCalculator implements ScoreCalculationStrategy<Resume> {

    protected abstract ContainsMatcher getMatcher();

    @Override
    public int calculateScore(Resume resume) {
        Collection<WorkExperience> workExperiences = resume.getWorkExperiences();
        Collection<ProjectExperience> projectExperiences = resume.getProjectExperiences();
        Collection<VolunteerExperience> volunteerExperiences = resume.getVolunteerExperiences();
        int numStrongExperiences = getStrongExperiencesCount(workExperiences) +
            getStrongExperiencesCount(projectExperiences) +
            getStrongExperiencesCount(volunteerExperiences);
        int numExperiences = workExperiences.size() + projectExperiences.size() + volunteerExperiences.size();
        return (int) ((numStrongExperiences * 1.0 / numExperiences) * 15);
    }

    private <T extends Experience> int getStrongExperiencesCount(Collection<T> experiences) {
        return (int) experiences.stream().filter(experience -> isStrongExperience(experience)).count();
    }

    private boolean isStrongExperience(Experience experience) {
        String description = experience.getDescription();
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(description);
        int strongBullets = (int) bullets.stream().filter(bullet -> getMatcher().contains(bullet)).count();
        return (strongBullets * 1.0 / bullets.size()) > 0.5;
    }
}
