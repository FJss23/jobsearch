package com.fjss23.jobsearch.user;

import java.time.OffsetDateTime;

public record AppUserResponse(String firstName, String lastName, String email, String role, OffsetDateTime createdAt) {}
