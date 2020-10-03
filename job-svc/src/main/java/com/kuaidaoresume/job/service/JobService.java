package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.*;
import java.util.List;
import java.util.Optional;

public interface JobService {

    public Optional<JobDto> findJobById(long id);

    public Optional<JobDto> findJobByUuid(String uuid);

    public Optional<SimpleJobDto> findSimpleJobByUuid(String uuid);

    public void deleteJobById(long id);

    public List<JobDto> findAll();

    public void deleteAllJobs();

//    List<JobDto> findJobsByKeywords(List<KeywordDto> Keywords);
//
    public List<JobDto> findJobByLocation(List<LocationDto> locationDtos);

    public List<JobDto> findJobByMajor(List<MajorDto> majorDtos);

    public List<JobDto> findJobByLocationAndMajor(List<LocationDto> locationDtos, List<MajorDto> majorDtos);

    public Optional<JobDto> createJob(JobDto jobDto);

    public Optional<JobDto> updateJob(JobDto jobDto);

}
