package com.fjss23.jobsearch.joboffer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsGreaterOrEqualValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsGreaterOrEqual {
    String message() default "The second field is lower than the first field";

    String field();

    String fieldCheck();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
