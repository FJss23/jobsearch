package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.ApiV1PrefixController;
import com.fjss23.jobsearch.job.payload.JobRequest;
import com.fjss23.jobsearch.job.payload.JobRequestMapper;
import com.fjss23.jobsearch.job.payload.JobResponse;
import com.fjss23.jobsearch.job.payload.JobResponseMapper;
import com.fjss23.jobsearch.job.scrapping.JobScrappingService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@ApiV1PrefixController
public class JobController {

    private final JobService jobService;
    private final JobResponseMapper jobResponseMapper;
    private final JobRequestMapper jobRequestMapper;
    private final JobScrappingService jobScrappingService;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    public JobController(
            JobService jobService,
            JobScrappingService jobScrappingService,
            JobResponseMapper jobResponseMapper,
            JobRequestMapper jobRequestMapper) {
        this.jobService = jobService;
        this.jobResponseMapper = jobResponseMapper;
        this.jobRequestMapper = jobRequestMapper;
        this.jobScrappingService = jobScrappingService;
    }

    @GetMapping("/jobs")
    public List<JobResponse> getJobs(@RequestParam("from") Long from, @RequestParam("size") int size) {
        List<Job> jobsPaginated = jobService.findPaginated(from, size);
        return jobsPaginated.stream().map(jobResponseMapper).collect(Collectors.toList());
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

    @GetMapping("/scrapping")
    public void manualScrapping() {
        jobScrappingService.scrappingFromHackerNews();
    }
}
