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
        String wkDay,
        String description,
        String state,
        String wkModel,
        OffsetDateTime createdAt,
        List<TagResponse> tags) {}
