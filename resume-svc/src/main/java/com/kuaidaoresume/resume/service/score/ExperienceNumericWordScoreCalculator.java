package com.kuaidaoresume.resume.service.score;

import com.kuaidaoresume.common.matching.ContainsMatcher;
import com.kuaidaoresume.common.matching.NumericWordMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExperienceNumericWordScoreCalculator extends AbstractExperienceStrengthScoreCalculator {

    @Autowired
    private final NumericWordMatcher numericWordMatcher;

    @Override
    protected ContainsMatcher getMatcher() {
        return numericWordMatcher;
    }
}
