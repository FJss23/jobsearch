package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.job.Company;
import com.fjss23.jobsearch.job.Salary;
import com.fjss23.jobsearch.tag.payload.TagResponse;
import java.time.OffsetDateTime;
import java.util.List;

public record JobResponse(
        Long id,
        String title,
        String role,
        Salary salary,
        String location,
        String workday,
        String description,
        Company company,
        String state,
        String workModel,
        OffsetDateTime createdAt,
        List<TagResponse> tags) {}
