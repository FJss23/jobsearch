package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.tag.payload.TagResponse;
import java.time.OffsetDateTime;
import java.util.List;

public record JobResponse(
        Long id,
        String title,
        String role,
        Double salaryFrom,
        Double salaryUpTo,
        String salaryCurrency,
        String location,
        String workday,
        String description,
        String companyName,
        String state,
        String workModel,
        OffsetDateTime createdAt,
        List<TagResponse> tags) {}
