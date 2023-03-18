package com.fjss23.jobsearch.company;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        String twitter,
        String facebook,
        String instagram,
        String website) {}
