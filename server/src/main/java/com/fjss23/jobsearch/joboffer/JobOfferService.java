package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.company.CompanyService;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagService;
import java.util.List;
import org.springframework.stereotype.Service;

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

    List<JobOffer> findAll() {
        List<JobOffer> jobs = jobOfferRepository.findAll();

        for (JobOffer job : jobs) {
            Company company =
                    companyService.getCompanyById(job.getCompany().getId()).get();
            job.setCompany(company);
            List<Tag> tags = tagService.getTagsOfJobOffer(job.getId());
            job.setTags(tags);
        }

        return jobs;
    }
}
