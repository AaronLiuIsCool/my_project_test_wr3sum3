package com.kuaidaoresume.job.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaidaoresume.job.dto.*;
import com.kuaidaoresume.job.model.Job;
import com.kuaidaoresume.job.model.Location;
import com.kuaidaoresume.job.model.Major;
import com.kuaidaoresume.job.repository.JobRepository;
import com.kuaidaoresume.job.repository.LocationRepository;
import com.kuaidaoresume.job.repository.MajorRepository;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Optional<JobDto> findJobById(long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(modelMapper.map(jobOptional.get(), JobDto.class));
    }

    @Override
    public void deleteJobById(long id) {
       jobRepository.deleteById(id);
    }

    @Override
    public List<JobDto> findAll(){
        List<Job> jobs = jobRepository.findAll();
        List<JobDto> jobDtos = jobs.stream()
                .map(x -> modelMapper.map(x, JobDto.class))
                .collect(Collectors.toList());
        return jobDtos;
    }

    @Override
    public List<JobDto> findJobByLocation(List<LocationDto> locationDtos) {
        List<Location> locations = locationDtos.stream()
                .map(x -> locationRepository.findByCountryIgnoreCaseAndCityIgnoreCaseAndPostCodeIgnoreCase(x.getCountry(), x.getCity(), x.getPostCode()))
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
    public  Optional<JobDto>  saveJob(JobDto jobDto) {

        Optional<Job> jobOptional = jobRepository.findByUrl(jobDto.getUrl());
        if (jobOptional.isPresent()) {
            return Optional.ofNullable(null);
        }

        Job job = modelMapper.map(jobDto, Job.class);

        JobDto savedJobDto = modelMapper.map(jobRepository.save(job), JobDto.class);

        return Optional.ofNullable(savedJobDto);

    }

}
