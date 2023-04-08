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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Page<JobResponse> getJobs(
            @RequestParam(value = "from", required = false) Long from, @RequestParam("size") int size) {
        int totalJobs = jobService.getTotalJobs();
        PageRequest pageable = PageRequest.of(Math.toIntExact(from == null ? 0 : from), size);
        List<Job> jobs = jobService.findPaginated(from, size);
        List<JobResponse> jobsResponse = jobs.stream().map(jobResponseMapper).collect(Collectors.toList());
        return new PageImpl<>(jobsResponse, pageable, totalJobs);
    }

    @GetMapping("/jobs/{id}")
    public JobResponse getById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return jobResponseMapper.apply(job);
    }

    @PreAuthorize("hasAuthority('APP_ADMIN')")
    @DeleteMapping("/jobs/{id}")
    public Long deleteById(@PathVariable Long id) {
        return jobService.deleteById(id);
    }

    @PreAuthorize("hasAuthority('APP_ADMIN')")
    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody JobRequest jobRequest, Authentication authentication) {
        String email = authentication.getName();

        Job jobReceived = jobRequestMapper.apply(jobRequest);
        Job jobCreated = jobService.save(jobReceived);

        return jobResponseMapper.apply(jobCreated);
    }

    @PreAuthorize("hasAuthority('APP_ADMIN')")
    @GetMapping("/scrapping")
    public void manualScrapping() {
        jobScrappingService.scrappingFromHackerNews();
    }
}
