package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.job.Job;
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

        /*Set<JobWorkday> wkDays =
                        jobRequest.workday().stream().map(JobWorkday::valueOf).collect(Collectors.toUnmodifiableSet());
                Set<JobWorkModel> workModel =
                        jobRequest.workModel().stream().map(JobWorkModel::valueOf).collect(Collectors.toUnmodifiableSet());
        */
        return new Job(
                jobRequest.title(),
                jobRequest.role(),
                jobRequest.salaryFrom(),
                jobRequest.salaryUpTo(),
                jobRequest.salaryCurrency(),
                jobRequest.location(),
                jobRequest.workday(),
                jobRequest.description(),
                jobRequest.workModel(),
                jobRequest.companyName(),
                jobRequest.companyLogoUrl(),
                new HashSet<>(tags));
    }
}
