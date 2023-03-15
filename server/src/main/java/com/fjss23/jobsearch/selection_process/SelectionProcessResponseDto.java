package com.fjss23.jobsearch.selection_process;

import java.time.OffsetDateTime;

public record SelectionProcessResponseDto(
    String companyId,
    String jobofferId,
    Boolean discaredByCompany,
    OffsetDateTime lastTimeReviewed
) {}
