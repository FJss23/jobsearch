package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.ApiV1PrefixController;
import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.company.CompanyService;
import com.fjss23.jobsearch.scrapper.JobScrapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@ApiV1PrefixController
public class JobOfferController {

    private final JobOfferService jobOfferService;
    private final CompanyService companyService;

    private final JobOfferResponseMapper jobOfferResponseMapper;
    private final JobOfferRequestMapper jobOfferRequestMapper;

    private static final Logger logger = LoggerFactory.getLogger(JobOfferController.class);

    public JobOfferController(
            JobOfferService jobOfferService,
            CompanyService companyService,
            JobOfferResponseMapper jobOfferResponseMapper,
            JobOfferRequestMapper jobOfferRequestMapper) {
        this.jobOfferService = jobOfferService;
        this.companyService = companyService;
        this.jobOfferResponseMapper = jobOfferResponseMapper;
        this.jobOfferRequestMapper = jobOfferRequestMapper;
    }

    @GetMapping("/jobs")
    public List<JobOfferResponse> getAllJobs() {
        JobScrapper.scrap();
        return jobOfferService.findAll().stream().map(jobOfferResponseMapper).collect(Collectors.toList());
    }

    @GetMapping("/jobs/{id}")
    public JobOfferResponse getById(@PathVariable Long id) {
        JobOffer job = jobOfferService.findById(id);
        return jobOfferResponseMapper.apply(job);
    }

    @DeleteMapping("/jobs/{id}")
    public Long deleteById(@PathVariable Long id) {
        return jobOfferService.deleteById(id);
    }

    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public JobOfferResponse createJobOffer(
            @Valid @RequestBody JobOfferRequest jobOfferRequest, Authentication authentication) {
        String email = authentication.getName();
        Company company = companyService.getCompanyFromUser(email);

        JobOffer jobReceived = jobOfferRequestMapper.apply(jobOfferRequest);
        jobReceived.setCompany(company);

        JobOffer jobCreated = jobOfferService.createJobOffer(jobReceived);

        return jobOfferResponseMapper.apply(jobCreated);
    }
}
