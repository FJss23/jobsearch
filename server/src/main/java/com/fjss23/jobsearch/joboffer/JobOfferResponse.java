package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.CompanyResponse;
import com.fjss23.jobsearch.tag.TagResponse;
import java.time.OffsetDateTime;
import java.util.List;

public record JobOfferResponse(
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
