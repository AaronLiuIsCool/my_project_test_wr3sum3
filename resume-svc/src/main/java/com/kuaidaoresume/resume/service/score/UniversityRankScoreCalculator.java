package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.NameMatcher;
import com.kuaidaoresume.resume.model.Education;
import com.kuaidaoresume.resume.utils.FileUtil;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.*;

@Component
public class UniversityRankScoreCalculator implements ScoreCalculationStrategy<Education> {

    private final NameMatcher topTierUniversitiesMatcher;
    private final NameMatcher secondTierUniversitiesMatcher;

    public UniversityRankScoreCalculator() {
        topTierUniversitiesMatcher = new NameMatcher(FileUtil.loadLinesFromTextFile("matching/top-tier-universities.txt"));
        secondTierUniversitiesMatcher = new NameMatcher(FileUtil.loadLinesFromTextFile("matching/second-tier-universities.txt"));
    }

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
