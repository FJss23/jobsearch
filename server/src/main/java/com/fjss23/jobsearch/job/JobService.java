package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.company.CompanyService;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyService companyService;
    private final TagService tagService;

    public JobService(
        JobRepository jobRepository, CompanyService companyService, TagService tagService) {
        this.jobRepository = jobRepository;
        this.companyService = companyService;
        this.tagService = tagService;
    }

    public List<Job> findAll() {
        List<Job> jobs = jobRepository.findAll();

        for (Job job : jobs) {
            Company company = companyService.getCompanyById(job.getCompany().getId());
            job.setCompany(company);

            List<Tag> tags = tagService.getTagsOfJob(job.getId());
            job.setTags(tags);
        }

        return jobs;
    }

    public Job findById(Long id) {
        Job job =
                jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job offer not found"));

        Company company = companyService.getCompanyById(job.getId());
        job.setCompany(company);

        List<Tag> tags = tagService.getTagsOfJob(id);
        job.setTags(tags);

        return job;
    }

    @Transactional
    public Long deleteById(Long id) {
        tagService.deleteTagsOfJob(id);
        return jobRepository.deleteById(id);
    }

    @Transactional
    public Job createJob(Job job) {
        for (Tag tag : job.getTags()) {
            tagService.createTagsOfJob(tag.getId(), job.getId());
        }
        job.setState(JobState.CREATED);
        return jobRepository.save(job);
    }
}
