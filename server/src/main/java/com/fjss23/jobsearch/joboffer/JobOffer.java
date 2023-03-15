package com.fjss23.jobsearch.joboffer;

import java.time.OffsetDateTime;

public record JobOffer(
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
    Boolean scrapped,

    OffsetDateTime createdAt,
    String createdBy,
    OffsetDateTime updatedAt,
    String updatedBy
) {}
