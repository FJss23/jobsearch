package com.fjss23.jobsearch.joboffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/jobs")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    private static final Logger logger = LoggerFactory.getLogger(
        JobOfferController.class
    );

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping
    public List<JobOffer> getAllJobs() {
        return jobOfferService.findAll();
    }
}
