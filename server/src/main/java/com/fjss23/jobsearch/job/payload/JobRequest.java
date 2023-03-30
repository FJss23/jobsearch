package com.fjss23.jobsearch.job.payload;

import com.fjss23.jobsearch.job.JobWorkday;
import com.fjss23.jobsearch.job.JobWorkModel;
import com.fjss23.jobsearch.job.validation.FieldsGreaterOrEqual;
import com.fjss23.jobsearch.job.validation.TypesCheck;
import com.fjss23.jobsearch.tag.payload.TagRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

@FieldsGreaterOrEqual(
        field = "salaryFrom",
        fieldCheck = "salaryUpTo",
        message = "Salary from is greater than salary up to")
public record JobRequest(
        @NotEmpty(message = "{error.not-empty.job.title}")
                @Size(min = 3, max = 200, message = "{error.size.job.title}")
                String title,
        @Size(min = 5, max = 200, message = "{error.size.job.industry}") String industry,
        @Positive(message = "{error.positive.job.salaryFrom}") @Max(value = 100_000_000, message = "{error.max.job.salaryFrom}")
                Double salaryFrom,
        @Positive(message = "{error.positive.job.salaryUpTo}") @Max(value = 100_000_001, message = "{error.max.job.salaryUpTo}")
                Double salaryUpTo,
        String coin,
        @NotEmpty(message = "{error.not-empty.job.location}")
                @Size(min = 5, max = 200, message = "{error.size.job.location}")
                String location,
        @TypesCheck(enumClass = JobWorkday.class) String workday,
        @NotEmpty(message = "{error.not-empty.job.description")
                @Size(min = 10, max = 5000, message = "{error.size.job.description")
                String description,
        @TypesCheck(enumClass = JobWorkModel.class) String workplaceSystem,
        @Size(min = 0, max = 500, message = "{error.size.job.howToApply}") String howToApply,
        @NotEmpty(message = "{error.not-empty.job.tags}") List<TagRequest> tags) {}
