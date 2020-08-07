package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Resume;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.DEFAULT_MAX_PROJECT_EXPERIENCES_SCORE;

@Component
public class ProjectExperiencesScoreCalculator implements ScoreCalculationStrategy<Resume> {

    @Override
    public int calculateScore(Resume resume) {
        int numProjectExperiences = resume.getProjectExperiences().size();
        int maxProjectExperiencesScore = ScoreCalculationStrategy.getScoreConfig("max.project.experiences.score",
            DEFAULT_MAX_PROJECT_EXPERIENCES_SCORE);
        return numProjectExperiences > 1 ? maxProjectExperiencesScore : numProjectExperiences;
    }
}
