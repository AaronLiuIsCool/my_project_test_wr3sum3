package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.NameMatcher;
import com.kuaidaoresume.resume.model.WorkExperience;
import com.kuaidaoresume.common.utils.FileUtil;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.score.ScoreConstants.*;

@Component
public class CompanyRankScoreCalculator implements ScoreCalculationStrategy<WorkExperience> {

    private final NameMatcher topTierCompaniesMatcher;
    private final NameMatcher secondTierCompaniesMatcher;

    public CompanyRankScoreCalculator() {
        topTierCompaniesMatcher = new NameMatcher(FileUtil.loadLinesFromTextFile("matching/top-tier-companies.txt"));
        secondTierCompaniesMatcher = new NameMatcher(FileUtil.loadLinesFromTextFile("matching/second-tier-companies.txt"));
    }

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
