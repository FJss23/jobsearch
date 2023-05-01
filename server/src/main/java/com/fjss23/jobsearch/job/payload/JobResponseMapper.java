package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.job.Job;
import com.fjss23.jobsearch.tag.payload.TagResponse;
import com.fjss23.jobsearch.tag.payload.TagResponseMapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class JobResponseMapper implements Function<Job, JobResponse> {

    private final TagResponseMapper tagResponseMapper;

    public JobResponseMapper(TagResponseMapper tagResponseMapper) {
        this.tagResponseMapper = tagResponseMapper;
    }

    @Override
    public JobResponse apply(Job job) {
        List<TagResponse> tagsResponse =
                job.getTags().stream().map(tagResponseMapper).collect(Collectors.toList());

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getRole(),
                job.getSalary(),
                job.getLocation(),
                job.getWorkday(),
                job.getDescription(),
                job.getCompany(),
                job.getState().name(),
                job.getWorkModel(),
                job.getCreatedAt(),
                tagsResponse);
    }
}
