package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Resume;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.*;

@Component
public class CompletenessScoreCalculator implements ScoreCalculationStrategy<Resume> {

    @Override
    public int calculateScore(Resume resume) {
        int score = 0;
        if (resume.getBasicInfo() != null) {
            score += ScoreCalculationStrategy.getScoreConfig("basic.info.completeness.score",
                DEFAULT_BASIC_INFO_COMPLETENESS_SCORE);
        }
        if (resume.getEducations().size() > 0) {
            score += ScoreCalculationStrategy.getScoreConfig("education.completeness.score",
                DEFAULT_EDUCATION_COMPLETENESS_SCORE);
        }
        if (resume.getWorkExperiences().size() > 0) {
            score += ScoreCalculationStrategy.getScoreConfig("work.experience.completeness.score",
                DEFAULT_WORK_EXPERIENCE_COMPLETENESS_SCORE);
        }
        if (resume.getProjectExperiences().size() > 0) {
            score += ScoreCalculationStrategy.getScoreConfig("project.experience.completeness.score",
                DEFAULT_PROJECT_EXPERIENCE_COMPLETENESS_SCORE);
        }
        if (resume.getVolunteerExperiences().size() > 0) {
            score += ScoreCalculationStrategy.getScoreConfig("volunteer.experience.completeness.score",
                DEFAULT_VOLUNTEER_EXPERIENCE_COMPLETENESS_SCORE);
        }
        return score;
    }
}
