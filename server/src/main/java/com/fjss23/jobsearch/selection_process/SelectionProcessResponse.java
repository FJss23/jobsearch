package com.fjss23.jobsearch.selection_process;

import java.time.OffsetDateTime;

public record SelectionProcessResponse(
    String companyId, String jobId, Boolean discardedByCompany, OffsetDateTime lastTimeReviewed) {}
