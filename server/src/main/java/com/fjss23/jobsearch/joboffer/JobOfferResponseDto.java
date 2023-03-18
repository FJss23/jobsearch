package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.CompanyResponseDto;
import com.fjss23.jobsearch.tag.TagResponseDto;

import java.time.OffsetDateTime;
import java.util.List;

public record JobOfferResponseDto(
    Long id,
    String title,
    String location,
    String state,
    String workplaceType,
    OffsetDateTime createdAt,
    CompanyResponseDto company,
    List<TagResponseDto> tags
) {
}
