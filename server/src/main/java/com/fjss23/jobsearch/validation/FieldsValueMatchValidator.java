package com.fjss23.jobsearch.validation;

import com.fjss23.jobsearch.registration.RegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<FieldsValueMatch, RegistrationRequestDto> {

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) { }

    @Override
    public boolean isValid(RegistrationRequestDto request, ConstraintValidatorContext constraintValidatorContext) {
        return request.password() != null && request.repeatPassword() != null && !request.password().equals(request.repeatPassword());
    }
}
