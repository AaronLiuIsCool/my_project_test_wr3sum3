package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.NameMatcher;
import com.kuaidaoresume.resume.model.Education;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.*;

@Component
@RequiredArgsConstructor
public class UniversityRankScoreCalculator implements ScoreCalculationStrategy<Education> {

    @Autowired
    private final NameMatcher topTierUniversitiesMatcher;
    @Autowired
    private final NameMatcher secondTierUniversitiesMatcher;

    @Override
    public int calculateScore(Education education) {
        String institution = education.getInstitution();
        if (topTierUniversitiesMatcher.contains(institution)) {
            return ScoreCalculationStrategy.getScoreConfig("top.tier.university.score",
                DEFAULT_TOP_TIER_UNIVERSITY_SCORE);
        } else if (secondTierUniversitiesMatcher.contains(institution)) {
            return ScoreCalculationStrategy.getScoreConfig("second.tier.university.score",
                DEFAULT_SECOND_TIER_UNIVERSITY_SCORE);
        } else {
            return ScoreCalculationStrategy.getScoreConfig("other.university.score",
                DEFAULT_OTHER_UNIVERSITY_SCORE);
        }
    }
}
