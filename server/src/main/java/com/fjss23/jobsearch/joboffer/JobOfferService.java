package com.fjss23.jobsearch.joboffer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobOfferSerive {

    private final JobOfferRepository jobOfferRepository;

    public JobOfferSerive(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    List<JobOffer> findAll() {
        return jobOfferRepository.findAll();
    }
}
