package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.ApiV1PrefixController;
import com.fjss23.jobsearch.job.payload.JobRequest;
import com.fjss23.jobsearch.job.payload.JobRequestMapper;
import com.fjss23.jobsearch.job.payload.JobResponse;
import com.fjss23.jobsearch.job.payload.JobResponseMapper;
import com.fjss23.jobsearch.job.scrapping.JobScrappingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@ApiV1PrefixController
public class JobController {

    private final JobService jobService;
    private final JobResponseMapper jobResponseMapper;
    private final JobRequestMapper jobRequestMapper;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    public JobController(
            JobService jobService,
            JobScrappingService jobScrappingService,
            JobResponseMapper jobResponseMapper,
            JobRequestMapper jobRequestMapper) {
        this.jobService = jobService;
        this.jobResponseMapper = jobResponseMapper;
        this.jobRequestMapper = jobRequestMapper;
    }

    @GetMapping("/jobs")
    public List<JobResponse> getAllJobs() {
        return jobService.findAll().stream().map(jobResponseMapper).collect(Collectors.toList());
    }

    @GetMapping("/jobs/{id}")
    public JobResponse getById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return jobResponseMapper.apply(job);
    }

    @DeleteMapping("/jobs/{id}")
    public Long deleteById(@PathVariable Long id) {
        return jobService.deleteById(id);
    }

    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody JobRequest jobRequest, Authentication authentication) {
        String email = authentication.getName();

        Job jobReceived = jobRequestMapper.apply(jobRequest);
        Job jobCreated = jobService.save(jobReceived);

        return jobResponseMapper.apply(jobCreated);
    }
}
