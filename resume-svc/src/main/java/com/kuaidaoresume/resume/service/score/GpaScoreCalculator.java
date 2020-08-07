package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Education;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.*;

@Component
public class GpaScoreCalculator implements ScoreCalculationStrategy<Education> {
    @Override
    public int calculateScore(Education education) {
        double gpa = Double.valueOf(education.getGpa());
        if (gpa > 3.5) {
            return ScoreCalculationStrategy.getScoreConfig("high.gpa.score",
                DEFAULT_HIGH_GPA_SCORE);
        } else if (gpa >= 3.0) {
            return ScoreCalculationStrategy.getScoreConfig("medium.gpa.score",
                DEFAULT_MEDIUM_GPA_SCORE);
        } else {
            return ScoreCalculationStrategy.getScoreConfig("low.gpa.score",
                DEFAULT_LOW_GPA_SCORE);
        }
    }
}
