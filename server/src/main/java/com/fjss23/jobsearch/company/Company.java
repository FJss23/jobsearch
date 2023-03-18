package com.fjss23.jobsearch.company;

import java.time.OffsetDateTime;

public record Company(
    Long id,
    String name,
    String description,
    String logoUrl,
    String twitter,
    String facebook,
    String instagram,
    String website,

    OffsetDateTime createdAt,
    String createdBy,
    OffsetDateTime updatedAt,
    String updatedBy
) {
}
