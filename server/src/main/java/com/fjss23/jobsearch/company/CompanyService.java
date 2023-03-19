package com.fjss23.jobsearch.company;

import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getCompanyById(Long id) {
        return companyRepository
                .getCompanyById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    public Company getCompanyFromUser(String email) {
        return companyRepository
                .getCompanyFromUser(email)
                .orElseThrow(() -> new IllegalArgumentException("The user doesn't belong to any company"));
    }
}
