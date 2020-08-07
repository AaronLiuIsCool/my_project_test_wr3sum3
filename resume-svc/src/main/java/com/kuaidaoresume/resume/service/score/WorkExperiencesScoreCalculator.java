package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Resume;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.DEFAULT_MAX_WORK_EXPERIENCES_SCORE;

@Component
public class WorkExperiencesScoreCalculator implements ScoreCalculationStrategy<Resume> {

    @Override
    public int calculateScore(Resume resume) {
        int numExperiences = resume.getWorkExperiences().size();
        int maxWorkExperienceScore = ScoreCalculationStrategy.getScoreConfig("max.work.experiences.score",
            DEFAULT_MAX_WORK_EXPERIENCES_SCORE);
        return numExperiences > maxWorkExperienceScore ? maxWorkExperienceScore : numExperiences;
    }
}
