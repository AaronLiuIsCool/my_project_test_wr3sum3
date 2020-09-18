package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.NameMatcher;
import com.kuaidaoresume.resume.model.WorkExperience;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.*;

@RequiredArgsConstructor
@Component
public class CompanyRankScoreCalculator implements ScoreCalculationStrategy<WorkExperience> {

    @Autowired
    private final NameMatcher topTierCompaniesMatcher;
    @Autowired
    private final NameMatcher secondTierCompaniesMatcher;

    @Override
    public int calculateScore(WorkExperience workExperience) {
        String companyName = workExperience.getOrganization();
        if (topTierCompaniesMatcher.contains(companyName)) {
            int topTierCompanyScore = ScoreCalculationStrategy.getScoreConfig("top.tier.company.score",
                DEFAULT_TOP_TIER_COMPANY_SCORE);
            return topTierCompanyScore;
        } else if (secondTierCompaniesMatcher.contains(companyName)) {
            int secondTierCompanyScore = ScoreCalculationStrategy.getScoreConfig("second.tier.company.score",
                DEFAULT_SECOND_TIER_COMPANY_SCORE);
            return secondTierCompanyScore;
        } else {
            int otherCompanyScore = ScoreCalculationStrategy.getScoreConfig("other.company.score",
                DEFAULT_OTHER_COMPANY_SCORE);
            return otherCompanyScore;
        }
    }
}
