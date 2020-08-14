package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.*;
import java.util.List;
import java.util.Optional;

public interface JobService {

    public Optional<JobDto> findJobById(long id);

    public void deleteJobById(long id);

    public List<JobDto> findAll();

//    List<JobDto> findJobsByKeywords(List<KeywordDto> Keywords);
//
//    List<JobDto> findJobsByLocation(LocationDto locationDto);

    public List<JobDto> findJobByLocation(List<LocationDto> locationDtos);

    public List<JobDto> findJobByMajor(List<MajorDto> majorDtos);

    public Optional<JobDto> createJob(JobDto jobDto);

    public Optional<JobDto> updateJob(JobDto jobDto);

}
