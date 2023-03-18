package com.fjss23.jobsearch.joboffer;

import java.util.List;

import com.fjss23.jobsearch.company.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final CompanyService companyService;

    public JobOfferService(JobOfferRepository jobOfferRepository, CompanyService companyService) {
        this.jobOfferRepository = jobOfferRepository;
        this.companyService = companyService;
    }

    List<JobOffer> findAll() {
        return jobOfferRepository.findAll();
    }

    public List<JobOffer> getJobOffersDescription() {
        List<JobOffer> jobs =  jobOfferRepository.getJobsDescription();

        for (JobOffer job: jobs) {

        }
    }
}
