package com.fjss23.jobsearch.job.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldsGreaterOrEqualValidator implements ConstraintValidator<FieldsGreaterOrEqual, Object> {

    private String field;
    private String fieldCheck;

    @Override
    public void initialize(FieldsGreaterOrEqual constraintAnnotation) {
        this.field = String.valueOf(constraintAnnotation.field());
        this.fieldCheck = String.valueOf(constraintAnnotation.fieldCheck());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        var fieldValue = (Double) new BeanWrapperImpl(value).getPropertyValue(field);
        var fieldCheckValue = (Double) new BeanWrapperImpl(value).getPropertyValue(fieldCheck);

        if (fieldValue != null && fieldCheckValue != null) {
            return fieldValue <= fieldCheckValue;
        }
        return true;
    }
}
