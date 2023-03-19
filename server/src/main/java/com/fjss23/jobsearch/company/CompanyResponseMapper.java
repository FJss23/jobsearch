package com.fjss23.jobsearch.company;

import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class CompanyResponseMapper implements Function<Company, CompanyResponse> {

    @Override
    public CompanyResponse apply(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getTwitter(),
                company.getFacebook(),
                company.getInstagram(),
                company.getWebsite());
    }
}
