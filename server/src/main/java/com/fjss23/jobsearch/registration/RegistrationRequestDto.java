package com.fjss23.jobsearch.registration;

public record RegistrationRequestDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String repeatPassword
) { }
