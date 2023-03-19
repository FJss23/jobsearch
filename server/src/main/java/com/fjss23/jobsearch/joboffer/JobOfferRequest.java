package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.joboffer.types.JobOfferWorkDay;
import com.fjss23.jobsearch.joboffer.types.JobOfferWorkplaceSystem;
import com.fjss23.jobsearch.joboffer.validation.FieldsGreaterOrEqual;
import com.fjss23.jobsearch.joboffer.validation.TypesCheck;
import com.fjss23.jobsearch.tag.TagRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

@FieldsGreaterOrEqual(
        field = "salaryFrom",
        fieldCheck = "salaryUpTo",
        message = "Salary from is greater than salary up to")
public record JobOfferRequest(
        @NotEmpty(message = "{error.not-empty.joboffer.title}")
                @Size(min = 3, max = 200, message = "{error.size.joboffer.title}")
                String title,
        @Size(min = 5, max = 200, message = "{error.size.joboffer.industry}") String industry,
        @Positive(message = "{error.positive.joboffer.salaryFrom}") @Max(value = 100_000_000, message = "{error.max.joboffer.salaryFrom}")
                Double salaryFrom,
        @Positive(message = "{error.positive.joboffer.salaryUpTo}") @Max(value = 100_000_001, message = "{error.max.joboffer.salaryUpTo}")
                Double salaryUpTo,
        String coin,
        @NotEmpty(message = "{error.not-empty.joboffer.location}")
                @Size(min = 5, max = 200, message = "{error.size.joboffer.location}")
                String location,
        @TypesCheck(enumClass = JobOfferWorkDay.class) String workday,
        @NotEmpty(message = "{error.not-empty.joboffer.description")
                @Size(min = 10, max = 5000, message = "{error.size.joboffer.description")
                String description,
        @TypesCheck(enumClass = JobOfferWorkplaceSystem.class) String workplaceSystem,
        @Size(min = 0, max = 500, message = "{error.size.joboffer.howToApply}") String howToApply,
        @NotEmpty(message = "{error.not-empty.joboffer.tags}") List<TagRequest> tags) {}
