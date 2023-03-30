package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.company.payload.CompanyResponse;
import com.fjss23.jobsearch.company.payload.CompanyResponseMapper;
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
    private final CompanyResponseMapper companyResponseMapper;

    public JobResponseMapper(TagResponseMapper tagResponseMapper, CompanyResponseMapper companyResponseMapper) {
        this.tagResponseMapper = tagResponseMapper;
        this.companyResponseMapper = companyResponseMapper;
    }

    @Override
    public JobResponse apply(Job job) {
        CompanyResponse companyResponse = companyResponseMapper.apply(job.getCompany());

        List<TagResponse> tagsResponse =
                job.getTags().stream().map(tagResponseMapper).collect(Collectors.toList());

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getIndustry(),
                job.getSalaryFrom(),
                job.getSalaryFrom(),
                job.getCoin(),
                job.getLocation(),
                job.getWorkday().name(),
                job.getDescription(),
                job.getState().name(),
                job.getWorkModel().name(),
                job.getHowToApply(),
                job.getCreatedAt(),
                companyResponse,
                tagsResponse);
    }
}
