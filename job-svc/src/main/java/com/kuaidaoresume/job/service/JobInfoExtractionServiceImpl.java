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
import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;
import java.util.stream.*;
import java.time.*;

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

        List<KeywordDto> keywordDtos = keywordMatcher.getMatches(jobFetcherRequest.getDescription())
                .stream().map(x -> KeywordDto.builder()
                        .name(x)
                        .build()).collect(Collectors.toList());

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

        if (jobOptional.isPresent()) {
            return null;
        }
        Job job = Job.builder().postDate(createdAt)
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

        List<Keyword> keywordsToSave = new ArrayList<>();

        for(KeywordDto keywordDto : keywordDtos) {
            Optional<Keyword> keywordOptional = keywordRepository.findByName(keywordDto.getName());
            Keyword keyword;
            if(keywordOptional.isPresent()) {
                keyword = keywordOptional.get();
            }
            else {
                keyword = modelMapper.map(keywordDto, Keyword.class);
                keywordsToSave.add(keyword);
            }
            JobHasKeyword rating = JobHasKeyword
                    .builder()
                    .job(job).keyword(keyword)
                    .rating(1.0)
                    .build();
            job.getJobHasKeywords().add(rating); //set
        }
        if(keywordsToSave.size() > 0) {
            keywordRepository.saveAll(keywordsToSave);
        }
        job = jobRepository.save(job);


        JobFetcherResponse jobFetcherResponse = JobFetcherResponse.builder()
                .uuid(job.getUuid())
                .postDate(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault()))
                .title(jobFetcherRequest.getTitle())
                .companyName(jobFetcherRequest.getCompanyName())
                .location(locationDto)
                .jobType(jobType)
                .relevantMajors(majorDtos.stream().map(majorDto -> majorDto.getName()).collect(Collectors.toList()))
                .keywords(keywordDtos)
                .build();

        return jobFetcherResponse;
    }
}
