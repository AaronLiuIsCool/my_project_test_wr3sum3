package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.resume.model.Experience;
import com.kuaidaoresume.resume.utils.DateUtil;

import java.sql.Date;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.DEFAULT_MAX_EXPERIENCE_DURATION_SCORE;

public abstract class AbstractExperienceDurationScoreCalculator<T extends Experience> implements ScoreCalculationStrategy<T> {

    @Override
    public int calculateScore(T experience) {
        Date startDate = experience.getStartDate();
        Date endDate = experience.getEndDate();

        int durationInMonths = DateUtil.getDurationInMonths(startDate, endDate);
        int maxExperienceDurationScore = ScoreCalculationStrategy.getScoreConfig("max.experience.duration.score",
            DEFAULT_MAX_EXPERIENCE_DURATION_SCORE);
        return durationInMonths > maxExperienceDurationScore ? maxExperienceDurationScore : durationInMonths;
    }
}
