package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.ContainsMatcher;
import com.kuaidaoresume.common.matching.KeywordMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExperienceKeywordScoreCalculator extends AbstractExperienceStrengthScoreCalculator {

    @Autowired
    private final KeywordMatcher keywordMatcher;

    @Override
    protected ContainsMatcher getMatcher() {
        return keywordMatcher;
    }
}
