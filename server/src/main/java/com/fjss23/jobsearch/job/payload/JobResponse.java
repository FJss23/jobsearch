package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.company.payload.CompanyResponse;
import com.fjss23.jobsearch.tag.payload.TagResponse;
import java.time.OffsetDateTime;
import java.util.List;

public record JobResponse(
        Long id,
        String title,
        String industry,
        Double salaryFrom,
        Double salaryUpTo,
        String coin,
        String location,
        String workday,
        String description,
        String state,
        String workplaceSystem,
        String howToApply,
        OffsetDateTime createdAt,
        CompanyResponse company,
        List<TagResponse> tags) {}
