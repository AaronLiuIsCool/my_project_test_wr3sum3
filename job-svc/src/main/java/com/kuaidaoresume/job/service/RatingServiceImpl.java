package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.*;
import com.kuaidaoresume.job.repository.JobRepository;
import com.kuaidaoresume.job.repository.KeywordRepository;
import com.kuaidaoresume.job.repository.LocationRepository;
import com.kuaidaoresume.job.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService{

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final MajorRepository majorRepository;

    @Autowired
    private final KeywordRepository keywordRepository;

    @Override
    public Optional<JobHasKeywordDto> addJobRating(JobHasKeywordDto jobHasKeywordDto){
        JobDto jobDto = jobHasKeywordDto.getJob();
        Job job = modelMapper.map(jobDto, Job.class);

        KeywordDto keywordDto = jobHasKeywordDto.getKeyword();
        Keyword keyword = modelMapper.map(keywordDto, Keyword.class);

        JobHasKeyword rating = JobHasKeyword
                                .builder()
                                .job(job).keyword(keyword)
                                .rating(jobHasKeywordDto.getRating())
                                .build();

        job.getJobHasKeywords().add(rating);
        keywordRepository.save(keyword);
        jobRepository.save(job);

        return Optional.ofNullable(modelMapper.map(rating, JobHasKeywordDto.class));
    }

    @Override
    public Optional<JobHasKeywordDto> updateJobRating(JobHasKeywordDto jobHasKeywordDto){

        JobDto jobDto = jobHasKeywordDto.getJob();
        Optional<Job> existing = jobRepository.findByUrl(jobDto.getUrl());


        Job toUpdate = modelMapper.map(jobDto, Job.class);
        toUpdate.setId(existing.get().getId());

        Keyword keyword = keywordRepository.findByName(jobHasKeywordDto.getKeyword().getName()).get();

        toUpdate.getJobHasKeywords().removeIf(x -> x.getKeyword().getName().equals(jobHasKeywordDto.getKeyword().getName()));

        JobHasKeyword rating = JobHasKeyword
                .builder()
                .job(toUpdate).keyword(keyword)
                .rating(jobHasKeywordDto.getRating())
                .build();

        toUpdate.getJobHasKeywords().add(rating);

        jobRepository.save(toUpdate);

        return Optional.ofNullable(modelMapper.map(jobHasKeywordDto, JobHasKeywordDto.class));
    }

    @Override
    public Optional<JobHasKeywordDto> deleteJobRating(JobHasKeywordDto jobHasKeywordDto){

        JobDto jobDto = jobHasKeywordDto.getJob();
        Optional<Job> existing = jobRepository.findByUrl(jobDto.getUrl());


        Job toUpdate = modelMapper.map(jobDto, Job.class);
        toUpdate.setId(existing.get().getId());

        Keyword keyword = keywordRepository.findByName(jobHasKeywordDto.getKeyword().getName()).get();

        toUpdate.getJobHasKeywords().removeIf(x -> x.getKeyword().getName().equals(jobHasKeywordDto.getKeyword().getName()));

        jobRepository.save(toUpdate);

        return Optional.ofNullable(modelMapper.map(jobHasKeywordDto, JobHasKeywordDto.class));
    }

    @Override
    public Optional<LocationHasKeywordDto> addLocationRating(LocationHasKeywordDto locationHasKeywordDto){
        LocationDto locationDto = locationHasKeywordDto.getLocation();
        Location location = modelMapper.map(locationDto, Location.class);

        KeywordDto keywordDto = locationHasKeywordDto.getKeyword();
        Keyword keyword = modelMapper.map(keywordDto, Keyword.class);

        LocationHasKeyword rating = LocationHasKeyword
                .builder()
                .location(location).keyword(keyword)
                .rating(locationHasKeywordDto.getRating())
                .build();

        location.getLocationHasKeywords().add(rating);
        keywordRepository.save(keyword);
        locationRepository.save(location);

        return Optional.ofNullable(modelMapper.map(rating, LocationHasKeywordDto.class));
    }

    @Override
    public Optional<LocationHasKeywordDto> updateLocationRating(LocationHasKeywordDto locationHasKeywordDto){

        LocationDto locationDto = locationHasKeywordDto.getLocation();
        Optional<Location> existing =
                locationRepository.findByCountryIgnoreCaseAndCityIgnoreCase(
                        locationDto.getCountry(), locationDto.getCity());


        Location toUpdate = modelMapper.map(locationDto, Location.class);
        toUpdate.setId(existing.get().getId());

        Keyword keyword = keywordRepository.findByName(locationHasKeywordDto.getKeyword().getName()).get();

        toUpdate.getLocationHasKeywords().removeIf(x -> x.getKeyword().getName().equals(locationHasKeywordDto.getKeyword().getName()));

        LocationHasKeyword rating = LocationHasKeyword
                .builder()
                .location(toUpdate).keyword(keyword)
                .rating(locationHasKeywordDto.getRating())
                .build();

        toUpdate.getLocationHasKeywords().add(rating);

        locationRepository.save(toUpdate);

        return Optional.ofNullable(modelMapper.map(locationHasKeywordDto, LocationHasKeywordDto.class));
    }

    @Override
    public Optional<LocationHasKeywordDto> deleteLocationRating(LocationHasKeywordDto locationHasKeywordDto){

        LocationDto locationDto = locationHasKeywordDto.getLocation();
        Optional<Location> existing =
                locationRepository.findByCountryIgnoreCaseAndCityIgnoreCase(
                        locationDto.getCountry(), locationDto.getCity());


        Location toUpdate = modelMapper.map(locationDto, Location.class);
        toUpdate.setId(existing.get().getId());

        Keyword keyword = keywordRepository.findByName(locationHasKeywordDto.getKeyword().getName()).get();

        toUpdate.getLocationHasKeywords().removeIf(x -> x.getKeyword().getName().equals(locationHasKeywordDto.getKeyword().getName()));

        locationRepository.save(toUpdate);

        return Optional.ofNullable(modelMapper.map(locationHasKeywordDto, LocationHasKeywordDto.class));
    }


    @Override
    public Optional<MajorHasKeywordDto> addMajorRating(MajorHasKeywordDto majorHasKeywordDto){
        MajorDto majorDto = majorHasKeywordDto.getMajor();
        Major major = modelMapper.map(majorDto, Major.class);

        KeywordDto keywordDto = majorHasKeywordDto.getKeyword();
        Keyword keyword = modelMapper.map(keywordDto, Keyword.class);

        MajorHasKeyword rating = MajorHasKeyword
                .builder()
                .major(major).keyword(keyword)
                .rating(majorHasKeywordDto.getRating())
                .build();

        major.getMajorHasKeywords().add(rating);
        keywordRepository.save(keyword);
        majorRepository.save(major);

        return Optional.ofNullable(modelMapper.map(rating, MajorHasKeywordDto.class));
    }

    @Override
    public Optional<MajorHasKeywordDto> updateMajorRating(MajorHasKeywordDto majorHasKeywordDto){

        MajorDto majorDto = majorHasKeywordDto.getMajor();
        Optional<Major> existing =
                majorRepository.findByNameIgnoreCase(majorHasKeywordDto.getMajor().getName());


        Major toUpdate = modelMapper.map(majorDto, Major.class);
        toUpdate.setId(existing.get().getId());

        Keyword keyword = keywordRepository.findByName(majorHasKeywordDto.getKeyword().getName()).get();

        toUpdate.getMajorHasKeywords().removeIf(x -> x.getKeyword().getName().equals(majorHasKeywordDto.getKeyword().getName()));

        MajorHasKeyword rating = MajorHasKeyword
                .builder()
                .major(toUpdate).keyword(keyword)
                .rating(majorHasKeywordDto.getRating())
                .build();

        toUpdate.getMajorHasKeywords().add(rating);

        majorRepository.save(toUpdate);

        return Optional.ofNullable(modelMapper.map(rating, MajorHasKeywordDto.class));
    }

    @Override
    public Optional<MajorHasKeywordDto> deleteMajorRating(MajorHasKeywordDto majorHasKeywordDto){

        MajorDto majorDto = majorHasKeywordDto.getMajor();
        Optional<Major> existing =
                majorRepository.findByNameIgnoreCase(majorHasKeywordDto.getMajor().getName());


        Major toUpdate = modelMapper.map(majorDto, Major.class);
        toUpdate.setId(existing.get().getId());

        Keyword keyword = keywordRepository.findByName(majorHasKeywordDto.getKeyword().getName()).get();

        toUpdate.getMajorHasKeywords().removeIf(x -> x.getKeyword().getName().equals(majorHasKeywordDto.getKeyword().getName()));

        majorRepository.save(toUpdate);

        return Optional.ofNullable(modelMapper.map(majorHasKeywordDto, MajorHasKeywordDto.class));
    }
}
