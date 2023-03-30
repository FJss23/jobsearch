package com.fjss23.jobsearch.job.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {TypeCheckValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER
})
public @interface TypesCheck {
    String message() default "The field doesn't match the enum value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends java.lang.Enum<?>> enumClass();
}
