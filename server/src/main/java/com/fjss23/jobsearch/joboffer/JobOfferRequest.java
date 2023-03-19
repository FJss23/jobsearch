package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.tag.TagRequest;
import java.util.List;

public record JobOfferRequest(
        String title,
        String industry,
        Double salaryFrom,
        Double salaryUpTo,
        String coin,
        String location,
        String workday,
        String description,
        String workplaceSystem,
        String howToApply,
        List<TagRequest> tags) {}
