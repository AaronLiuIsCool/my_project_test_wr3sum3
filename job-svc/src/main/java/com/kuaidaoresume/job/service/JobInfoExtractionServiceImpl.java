package com.kuaidaoresume.job.service;

import com.kuaidaoresume.common.matching.KeywordMatcher;
import com.kuaidaoresume.job.dto.JobFetcherRequest;
import com.kuaidaoresume.job.dto.JobFetcherResponse;
import com.kuaidaoresume.job.model.Job;
import com.kuaidaoresume.job.model.Major;
import com.kuaidaoresume.job.model.JobHasKeyword;
import com.kuaidaoresume.job.model.Keyword;
import com.kuaidaoresume.job.model.Location;
import com.kuaidaoresume.job.repository.JobRepository;
import com.kuaidaoresume.job.repository.KeywordRepository;
import com.kuaidaoresume.job.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.kuaidaoresume.job.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.Date;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class JobInfoExtractionServiceImpl implements JobInfoExtractionService{
    @Autowired
    MajorExtractionService majorExtractionService;

    @Autowired
    MajorRepository majorRepository;

    @Autowired
    KeywordMatcher keywordMatcher;

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final KeywordRepository keywordRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public JobFetcherResponse extractAndPersist(JobFetcherRequest jobFetcherRequest) {
        String url = jobFetcherRequest.getJobLink();

        List<Keyword> keywords = keywordMatcher.getMatches(jobFetcherRequest.getDescription())
                .stream().map(x -> Keyword.builder()
                        .name(x)
                        .build()).collect(Collectors.toList());

        List<KeywordDto> keywordDtos = keywords.stream().map(keyword -> modelMapper.map(keyword, KeywordDto.class))
                .collect(Collectors.toList());

        String[] locationData = jobFetcherRequest.getLocation().split(",");

        LocationDto locationDto = LocationDto.builder()
                .country(locationData[locationData.length - 1].trim()) //last one is always country
                .city(locationData[0].trim()) //first one is always state
                .state(locationData.length == 3 ? "" : locationData[1].trim())
                .build();

        Location location = modelMapper.map(locationDto, Location.class);

        List<MajorDto> majorDtos = majorExtractionService.extract(jobFetcherRequest.getDescription());
        List<Major> majors = majorDtos.stream().map
                ( majorDto ->
                        majorRepository.findByNameIgnoreCase(majorDto.getName()).isPresent() ?
                                majorRepository.findByNameIgnoreCase(majorDto.getName()).get() : modelMapper.map(majorDto, Major.class)
        ).collect(Collectors.toList());

        List<String> extensions = jobFetcherRequest.getExtensions();

        long createdTimeMillis = System.currentTimeMillis();
        String jobType = "";

        if(extensions.size() == 2) {
            String age = extensions.get(0);
            int num =  Integer.parseInt(age.replaceAll("[^0-9]", ""));
            if (age.toLowerCase().contains("day")){
                createdTimeMillis -= 24 * 60 * 60 * 1000 * num;
            }
            else if(age.toLowerCase().contains("hour")) {
                createdTimeMillis -=  60 * 60 * 1000 * num;
            }
            jobType = extensions.get(1);
        }
        Date createdAt = new Date(createdTimeMillis);

        Optional<Job> jobOptional = jobRepository.findByUrl(url);

        Job job;
        if (jobOptional.isPresent()) {
            job = jobOptional.get();
        }
        else {
            job = Job.builder().postDate(createdAt)
                    .positionTitle(jobFetcherRequest.getTitle())
                    .companyName(jobFetcherRequest.getCompanyName())
                    .url(url)
                    .jobType(jobType)
                    .agency(jobFetcherRequest.getAgency())
                    .jobDescription(jobFetcherRequest.getDescription())
                    .jobPostIdentifier(jobFetcherRequest.getJobPostId())
                    .location(location)
                    .majors(majors)
                    .build();

            for(Keyword keyword : keywords) {
                Optional<Keyword> keywordOptional = keywordRepository.findByName(keyword.getName());
                if(keywordOptional.isPresent()) {
                    keyword = keywordOptional.get();
                }
                else {
                    keywordRepository.save(keyword);
                }
                JobHasKeyword rating = JobHasKeyword
                        .builder()
                        .job(job).keyword(keyword)
                        .rating(1.0)
                        .build();
                job.getJobHasKeywords().add(rating); //set
            }
            jobRepository.save(job);
        }

        JobFetcherResponse jobFetcherResponse = JobFetcherResponse.builder()
                .jobId(String.valueOf(job.getId()))
                .title(jobFetcherRequest.getTitle())
                .companyName(jobFetcherRequest.getCompanyName())
                .location(locationDto)
                .jobType(jobType)
                .keywords(keywordDtos)
                .build();

        return jobFetcherResponse;
    }
}
