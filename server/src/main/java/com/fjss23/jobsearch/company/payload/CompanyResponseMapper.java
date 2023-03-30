package com.fjss23.jobsearch.company.payload;

import java.util.function.Function;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.company.payload.CompanyResponse;
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
