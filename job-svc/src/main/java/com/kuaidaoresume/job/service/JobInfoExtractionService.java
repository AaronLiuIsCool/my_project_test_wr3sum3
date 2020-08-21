package com.kuaidaoresume.job.service;

import com.kuaidaoresume.job.dto.JobFetcherRequest;
import com.kuaidaoresume.job.dto.JobFetcherResponse;

public interface JobInfoExtractionService {
    public JobFetcherResponse extractAndPersist(JobFetcherRequest jobFetcherRequest);
}
