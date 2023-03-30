package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.job.Job;
import com.fjss23.jobsearch.job.JobWorkday;
import com.fjss23.jobsearch.job.JobWorkModel;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.payload.TagRequestMapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class JobRequestMapper implements Function<JobRequest, Job> {

    private final TagRequestMapper tagRequestMapper;

    public JobRequestMapper(TagRequestMapper tagRequestMapper) {
        this.tagRequestMapper = tagRequestMapper;
    }

    @Override
    public Job apply(JobRequest jobRequest) {
        List<Tag> tags = jobRequest.tags().stream().map(tagRequestMapper).collect(Collectors.toList());
        var company = new Company();
        return new Job(
                jobRequest.title(),
                jobRequest.industry(),
                jobRequest.salaryFrom(),
                jobRequest.salaryUpTo(),
                jobRequest.coin(),
                jobRequest.location(),
                JobWorkday.valueOf(jobRequest.workday()),
                jobRequest.description(),
                JobWorkModel.valueOf(jobRequest.workplaceSystem()),
                jobRequest.howToApply(),
                company,
                tags);
    }
}
