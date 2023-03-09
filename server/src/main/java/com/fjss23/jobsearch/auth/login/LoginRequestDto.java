package com.fjss23.jobsearch.auth.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
    @NotEmpty(message = "{error.not-empty.email}")
    @Email(message = "{error.bad-format.email}")
    String email,
    @NotEmpty(message = "{error.not-empty.password}")
    @Size(min = 6, max = 40, message = "{error.size.password}")
    String password
) {
}
