package com.fjss23.jobsearch.joboffer.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TypeCheckValidator implements ConstraintValidator<TypesCheck, Object> {

    private Object[] typesCheck;

    @Override
    public void initialize(TypesCheck constraintAnnotation) {
        this.typesCheck = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (null != value) {
            String contextValue = value.toString();

            for (Object enumValue : typesCheck) {
                if (enumValue.toString().equals(contextValue)) {
                    return true;
                }
            }
        }

        return false;
    }
}
