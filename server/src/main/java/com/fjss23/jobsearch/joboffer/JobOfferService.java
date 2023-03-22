package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.company.CompanyService;
import com.fjss23.jobsearch.joboffer.types.JobOfferState;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final CompanyService companyService;
    private final TagService tagService;

    public JobOfferService(
            JobOfferRepository jobOfferRepository, CompanyService companyService, TagService tagService) {
        this.jobOfferRepository = jobOfferRepository;
        this.companyService = companyService;
        this.tagService = tagService;
    }

    public List<JobOffer> findAll() {
        List<JobOffer> jobs = jobOfferRepository.findAll();

        for (JobOffer job : jobs) {
            Company company = companyService.getCompanyById(job.getCompany().getId());
            job.setCompany(company);

            List<Tag> tags = tagService.getTagsOfJobOffer(job.getId());
            job.setTags(tags);
        }

        return jobs;
    }

    public JobOffer findById(Long id) {
        JobOffer job =
                jobOfferRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job offer not found"));

        Company company = companyService.getCompanyById(job.getId());
        job.setCompany(company);

        List<Tag> tags = tagService.getTagsOfJobOffer(id);
        job.setTags(tags);

        return job;
    }

    @Transactional
    public Long deleteById(Long id) {
        tagService.deleteTagsOfJobOffer(id);
        return jobOfferRepository.deleteById(id);
    }

    @Transactional
    public JobOffer createJobOffer(JobOffer jobOffer) {
        for (Tag tag : jobOffer.getTags()) {
            tagService.createTagsOfJobOffer(tag.getId(), jobOffer.getId());
        }
        jobOffer.setState(JobOfferState.CREATED);
        return jobOfferRepository.save(jobOffer);
    }
}
