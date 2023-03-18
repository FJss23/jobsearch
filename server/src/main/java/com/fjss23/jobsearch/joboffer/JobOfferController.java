package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.ApiV1PrefixController;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@ApiV1PrefixController
public class JobOfferController {

    private final JobOfferService jobOfferService;
    private final JobOfferResponseMapper jobOfferResponseMapper;

    private static final Logger logger = LoggerFactory.getLogger(JobOfferController.class);

    public JobOfferController(JobOfferService jobOfferService, JobOfferResponseMapper jobOfferResponseMapper) {
        this.jobOfferService = jobOfferService;
        this.jobOfferResponseMapper = jobOfferResponseMapper;
    }

    @GetMapping("/jobs")
    public List<JobOfferResponse> getAllJobs() {
        return jobOfferService.findAll().stream().map(jobOfferResponseMapper).collect(Collectors.toList());
    }
}
