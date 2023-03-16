package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.ApiV1PrefixController;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@ApiV1PrefixController("job-offers")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    private static final Logger logger = LoggerFactory.getLogger(JobOfferController.class);

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping
    public List<JobOffer> getAllJobs() {
        var jobs = jobOfferService.findAll();
        logger.info("{}", jobs);
        return jobs;
    }
}
