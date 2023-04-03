package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.job.Job;
import com.fjss23.jobsearch.job.JobWorkModel;
import com.fjss23.jobsearch.job.JobWorkday;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.payload.TagRequestMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        List<Tag> tags = jobRequest.tags().stream().map(tagRequestMapper).toList();

        /*Set<JobWorkday> wkDays =
                jobRequest.workday().stream().map(JobWorkday::valueOf).collect(Collectors.toUnmodifiableSet());
        Set<JobWorkModel> wkModel =
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
                new HashSet<>(tags));
    }
}
