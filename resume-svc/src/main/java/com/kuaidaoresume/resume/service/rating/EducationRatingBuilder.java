package com.kuaidaoresume.resume.service.rating;

import com.kuaidaoresume.common.matching.NameMatcher;
import com.kuaidaoresume.resume.dto.EducationRatingDto;
import com.kuaidaoresume.resume.model.Education;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.kuaidaoresume.resume.service.rating.Rating.*;

@Component
@RequiredArgsConstructor
public class EducationRatingBuilder {

    @Autowired
    private final NameMatcher topTierUniversitiesMatcher;
    @Autowired
    private final NameMatcher secondTierUniversitiesMatcher;

    public EducationRatingDto getEducationRating(Education education) {
        String institution = education.getInstitution();
        return EducationRatingDto.builder()
            .institution(institution)
            .major(education.getMajor())
            .gpa(education.getGpa())
            .rating(getRating(institution))
            .build();
    }

    private Rating getRating(String institution) {
        if (topTierUniversitiesMatcher.contains(institution)) {
            return GREAT;
        } else if (secondTierUniversitiesMatcher.contains(institution)) {
            return GOOD;
        } else {
            return MEDIOCRE;
        }
    }
}
