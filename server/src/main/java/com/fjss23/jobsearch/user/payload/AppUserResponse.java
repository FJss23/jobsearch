package com.fjss23.jobsearch.user.payload;

import java.time.OffsetDateTime;

public record AppUserResponse(String firstName, String lastName, String email, String role, OffsetDateTime createdAt) {}
