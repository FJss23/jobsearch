package com.fjss23.jobsearch.company;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.getCompanyById(id);
    }
}
