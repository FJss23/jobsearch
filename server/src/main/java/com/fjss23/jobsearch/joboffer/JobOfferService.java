package com.fjss23.jobsearch.joboffer;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;

    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    List<JobOffer> findAll() {
        return jobOfferRepository.findAll();
    }
}
