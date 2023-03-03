package com.fjss23.jobsearch.registration;

import com.fjss23.jobsearch.user.validation.FieldsValueMatch;
import jakarta.validation.constraints.*;

@FieldsValueMatch.List(
    {
        @FieldsValueMatch(
            field = "password",
            fieldMatch = "repeatPassword",
            message = "Passwords do not match"
        ),
    }
)
public record RegistrationRequestDto(
    @NotEmpty(message = "{error.not-empty.firstName}")
    @Size(min = 2, max = 60, message = "{error.size.firstName}")
    String firstName,
    @NotEmpty(message = "{error.not-empty.lastName}")
    @Size(min = 2, max = 60, message = "{error.size.lastName}")
    String lastName,
    @NotEmpty(message = "{error.not-empty.email}")
    @Email(message = "{error.bad-format.email}")
    String email,
    @NotEmpty(message = "{error.not-empty.password}")
    @Size(min = 6, max = 40, message = "{error.size.password}")
    String password,
    @NotEmpty(message = "{error.not-empty.repeatPassword}")
    String repeatPassword
) {}
