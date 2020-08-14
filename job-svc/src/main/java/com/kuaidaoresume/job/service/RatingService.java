package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.MajorHasKeyword;

import java.util.Optional;

public interface RatingService {

    public Optional<JobHasKeywordDto> addJobRating(JobHasKeywordDto jobHasKeywordDto);

    public Optional<JobHasKeywordDto> updateJobRating(JobHasKeywordDto jobHasKeywordDto);

    public Optional<JobHasKeywordDto> deleteJobRating(JobHasKeywordDto jobHasKeywordDto);

    public Optional<LocationHasKeywordDto> addLocationRating(LocationHasKeywordDto locationHasKeywordDto);

    public Optional<LocationHasKeywordDto> updateLocationRating(LocationHasKeywordDto locationHasKeywordDto);

    public Optional<LocationHasKeywordDto> deleteLocationRating(LocationHasKeywordDto locationHasKeywordDto);

    public Optional<MajorHasKeywordDto> addMajorRating(MajorHasKeywordDto majorHasKeywordDto);

    public Optional<MajorHasKeywordDto> updateMajorRating(MajorHasKeywordDto majorHasKeywordDto);

    public Optional<MajorHasKeywordDto> deleteMajorRating(MajorHasKeywordDto majorHasKeywordDto);
}
