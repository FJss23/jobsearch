package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.ApiV1PrefixController;
import com.fjss23.jobsearch.Page;
import com.fjss23.jobsearch.job.payload.JobRequest;
import com.fjss23.jobsearch.job.payload.JobRequestMapper;
import com.fjss23.jobsearch.job.payload.JobResponse;
import com.fjss23.jobsearch.job.payload.JobResponseMapper;
import com.fjss23.jobsearch.job.scraping.JobScrapingService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@ApiV1PrefixController
public class JobController {

    private final JobService jobService;
    private final JobResponseMapper jobResponseMapper;
    private final JobRequestMapper jobRequestMapper;
    private final JobScrapingService jobScrapingService;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    public JobController(
            JobService jobService,
            JobScrapingService jobScrapingService,
            JobResponseMapper jobResponseMapper,
            JobRequestMapper jobRequestMapper) {
        this.jobService = jobService;
        this.jobResponseMapper = jobResponseMapper;
        this.jobRequestMapper = jobRequestMapper;
        this.jobScrapingService = jobScrapingService;
    }

    @GetMapping("/jobs")
    public Page<JobResponse> getJobs(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "from", required = false) Long from,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam("size") int size) {
        // Maybe I can get the number of elements left, knowing from where I'm starting
        var filters = new Filter(search, new ArrayList<>(), new ArrayList<>(), from, size, page);
        int totalJobs = jobService.getTotalJobs(filters);
        List<Job> jobs = jobService.findPaginated(filters);

        List<JobResponse> jobsResponse = jobs.stream().map(jobResponseMapper).collect(Collectors.toList());
        Long first = jobsResponse.isEmpty() ? null : jobsResponse.get(0).id();
        Long last = jobsResponse.isEmpty()
                ? null
                : jobsResponse.get(jobsResponse.size() - 1).id();
        var isFirstPage = from == null ? true : false;
        if (isFirstPage) first = null;

        return new Page<>(jobsResponse, first, last, totalJobs, isFirstPage);
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
    public JobResponse createJob(@Valid @RequestBody JobRequest jobRequest) {
        Job jobReceived = jobRequestMapper.apply(jobRequest);
        Job jobCreated = jobService.save(jobReceived);

        return jobResponseMapper.apply(jobCreated);
    }

    @PreAuthorize("hasAuthority('APP_ADMIN')")
    @GetMapping("/jobs/scrapping")
    public void manualHackerNewsScraping() {
        jobScrapingService.scrapingFromHackerNews();
    }

    @PreAuthorize("hasAuthority('APP_ADMIN')")
    @GetMapping("/jobs/set-source")
    public void manualSetSourceHackerNews() {
        jobScrapingService.scrappingUrlFromHackerNews();
    }
}
