package com.fjss23.jobsearch.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatcher {
    String message() default "Password and Repeat password must be the same";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
