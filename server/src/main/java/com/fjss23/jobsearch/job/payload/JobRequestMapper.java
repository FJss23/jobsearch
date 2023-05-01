package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.job.Company;
import com.fjss23.jobsearch.job.Job;
import com.fjss23.jobsearch.job.Salary;
import com.fjss23.jobsearch.job.scraping.ScrapingSource;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.payload.TagRequestMapper;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class JobRequestMapper implements Function<JobRequest, Job> {

    private final TagRequestMapper tagRequestMapper;

    public JobRequestMapper(TagRequestMapper tagRequestMapper) {
        this.tagRequestMapper = tagRequestMapper;
    }

    @Override
    public Job apply(JobRequest jobRequest) {
        List<Tag> tags = jobRequest.tags().stream().map(tagRequestMapper).toList();

        var company = new Company();
        company.setName(jobRequest.companyName());

        var salary = new Salary(jobRequest.salaryFrom(), jobRequest.salaryUpTo(), jobRequest.salaryCurrency());

        return new Job(
                jobRequest.title(),
                jobRequest.location(),
                jobRequest.workday(),
                jobRequest.description(),
                jobRequest.workModel(),
                salary,
                company,
                null,
                new ScrapingSource(),
                new HashSet<>(tags));
    }
}
