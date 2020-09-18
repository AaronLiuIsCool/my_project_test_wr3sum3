package com.kuaidaoresume.resume.service.rating;

import com.kuaidaoresume.common.matching.KeywordMatcher;
import com.kuaidaoresume.common.matching.NameMatcher;
import com.kuaidaoresume.common.matching.NumericWordMatcher;
import com.kuaidaoresume.resume.dto.BulletDto;
import com.kuaidaoresume.resume.dto.ExperienceRatingDto;
import com.kuaidaoresume.resume.model.Experience;
import com.kuaidaoresume.resume.utils.ResumeDescriptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.kuaidaoresume.resume.service.rating.Rating.*;

@Component
@RequiredArgsConstructor
public class ExperienceRatingBuilder {

    @Autowired
    private final NameMatcher topTierCompaniesMatcher;
    @Autowired
    private final NameMatcher secondTierCompaniesMatcher;
    @Autowired
    private final KeywordMatcher keywordMatcher;
    @Autowired
    private final NumericWordMatcher numericWordMatcher;

    public ExperienceRatingDto getExperienceRating(Experience experience) {
        String organization = experience.getOrganization();
        return ExperienceRatingDto.builder()
            .organization(organization)
            .role(experience.getRole())
            .rating(getRating(organization))
            .bullets(getBullets(experience.getDescription()))
            .build();
    }

    private Rating getRating(String organization) {
        if (topTierCompaniesMatcher.contains(organization)) {
            return GREAT;
        } else if (secondTierCompaniesMatcher.contains(organization)) {
            return GOOD;
        } else {
            return MEDIOCRE;
        }
    }

    private Collection<BulletDto> getBullets(String description) {
        return ResumeDescriptionUtil.splitByBullet(description).stream().map(bullet ->
            BulletDto.builder()
                .bullet(bullet)
                .keywords(keywordMatcher.getMatches(bullet))
                .numericWords(numericWordMatcher.getMatches(bullet))
                .build()
        ).collect(Collectors.toList());
    }
}
