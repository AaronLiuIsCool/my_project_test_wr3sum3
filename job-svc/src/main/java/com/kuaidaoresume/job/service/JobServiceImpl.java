package com.kuaidaoresume.job.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.Job;
import com.kuaidaoresume.job.model.Location;
import com.kuaidaoresume.job.model.Major;
import com.kuaidaoresume.job.repository.JobRepository;
import com.kuaidaoresume.job.repository.LocationRepository;
import com.kuaidaoresume.job.repository.MajorRepository;
import com.kuaidaoresume.job.config.CacheConfig;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService{

    static ILogger logger = SLoggerFactory.getLogger(JobService.class);

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final MajorRepository majorRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(cacheNames = CacheConfig.JOB_CACHE, key = "#id", unless = "#result == null")
    public Optional<JobDto> findJobById(long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(modelMapper.map(jobOptional.get(), JobDto.class));
    }

    @Override
    @Cacheable(cacheNames = CacheConfig.JOB_CACHE, key = "#uuid", unless = "#result == null")
    public Optional<JobDto> findJobByUuid(String uuid) {
        Optional<Job> jobOptional = jobRepository.findByUuid(uuid);
        if (!jobOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(modelMapper.map(jobOptional.get(), JobDto.class));
    }

    @Override
    @Cacheable(cacheNames = CacheConfig.JOB_CACHE, key = "#uuid", unless = "#result == null")
    public Optional<SimpleJobDto> findSimpleJobByUuid(String uuid) {
        Optional<Job> jobOptional = jobRepository.findByUuid(uuid);
        if (!jobOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(modelMapper.map(jobOptional.get(), SimpleJobDto.class));
    }

    @Override
    @Caching( evict = {
            @CacheEvict(cacheNames = CacheConfig.JOB_CACHE, key = "#id", beforeInvocation = true),
            @CacheEvict(cacheNames = CacheConfig.JOB_SEARCH_CACHE, allEntries=true, beforeInvocation = true)
    })
    public void deleteJobById(long id) {
       jobRepository.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = {CacheConfig.JOB_CACHE, CacheConfig.JOB_SEARCH_CACHE}, allEntries=true, beforeInvocation = true)
    public void deleteAllJobs() { jobRepository.deleteAll(); }

    @Override
    public List<JobDto> findAll(){
        List<Job> jobs = jobRepository.findAll();
        List<JobDto> jobDtos = jobs.stream()
                .map(x -> modelMapper.map(x, JobDto.class))
                .collect(Collectors.toList());
        return jobDtos;
    }

    @Override
    @Cacheable(CacheConfig.JOB_SEARCH_CACHE)
    public List<JobDto> findJobByLocationAndMajor(List<LocationDto> locationDtos, List<MajorDto> majorDtos) {
        List<Location> locations = locationDtos.stream()
                .map(x -> locationRepository.findByCountryIgnoreCaseAndCityIgnoreCase(x.getCountry(), x.getCity()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Major> majors = majorDtos.stream()
                .map(x -> majorRepository.findByNameIgnoreCase(x.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Job> jobs = jobRepository.findByLocationInAndMajorsIn(locations, majors);

        List<JobDto> jobDtos = jobs.stream()
                .map(x -> modelMapper.map(x, JobDto.class))
                .collect(Collectors.toList());
        return jobDtos;
    }

    @Override
    @Cacheable(CacheConfig.JOB_SEARCH_CACHE)
    public List<JobDto> findJobByLocation(List<LocationDto> locationDtos) {
        List<Location> locations = locationDtos.stream()
                .map(x -> locationRepository.findByCountryIgnoreCaseAndCityIgnoreCase(x.getCountry(), x.getCity()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Job> jobs = jobRepository.findByLocationIn(locations);

        List<JobDto> jobDtos = jobs.stream()
                .map(x -> modelMapper.map(x, JobDto.class))
                .collect(Collectors.toList());
        return jobDtos;
    }

    @Override
    @Cacheable(CacheConfig.JOB_SEARCH_CACHE)
    public List<JobDto> findJobByMajor(List<MajorDto> majorDtos) {
        List<Major> majors = majorDtos.stream()
                .map(x -> majorRepository.findByNameIgnoreCase(x.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Job> jobs = jobRepository.findByMajorsIn(majors);

        List<JobDto> jobDtos = jobs.stream()
                .map(x -> modelMapper.map(x, JobDto.class))
                .collect(Collectors.toList());
        return jobDtos;
    }

    @Override
    @Caching(
        put = {
            @CachePut(cacheNames = CacheConfig.JOB_CACHE, key = "#jobDto.getUuid()")
        },
        evict = {
                @CacheEvict(cacheNames = CacheConfig.JOB_SEARCH_CACHE, allEntries=true)
        })
    public  Optional<JobDto> createJob(JobDto jobDto) {
        Job job = modelMapper.map(jobDto, Job.class);

        JobDto savedJobDto = modelMapper.map(jobRepository.save(job), JobDto.class);

        return Optional.ofNullable(savedJobDto);
    }

    @Override
    @Caching(
            put = {
                    @CachePut(cacheNames = CacheConfig.JOB_CACHE, key = "#jobDto.getUuid()")
            },
            evict = {
                    @CacheEvict(cacheNames = CacheConfig.JOB_SEARCH_CACHE, allEntries=true)
            })
    public  Optional<JobDto> updateJob(JobDto jobDto) {
        Optional<Job> existing = jobRepository.findByUrl(jobDto.getUrl());
        if (existing.isPresent()) {
            Job toUpdate = modelMapper.map(jobDto, Job.class);
            toUpdate.setId(existing.get().getId());
            jobRepository.save(toUpdate);
            return Optional.ofNullable(modelMapper.map(toUpdate, JobDto.class));
        }
        else {
            return createJob((jobDto));
        }
    }
}
