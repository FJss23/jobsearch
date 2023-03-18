package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.company.CompanyResponse;
import com.fjss23.jobsearch.tag.TagResponse;
import com.fjss23.jobsearch.tag.TagResponseMapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class JobOfferResponseMapper implements Function<JobOffer, JobOfferResponse> {

    private final TagResponseMapper tagResponseMapper;

    public JobOfferResponseMapper(TagResponseMapper tagResponseMapper) {
        this.tagResponseMapper = tagResponseMapper;
    }

    @Override
    public JobOfferResponse apply(JobOffer jobOffer) {
        Company company = jobOffer.getCompany();
        var companyResponse = new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLogoUrl(),
                company.getTwitter(),
                company.getFacebook(),
                company.getInstagram(),
                company.getWebsite());

        List<TagResponse> tagsResponse =
                jobOffer.getTags().stream().map(tagResponseMapper).collect(Collectors.toList());

        return new JobOfferResponse(
                jobOffer.getId(),
                jobOffer.getTitle(),
                jobOffer.getIndustry(),
                jobOffer.getSalaryFrom(),
                jobOffer.getSalaryFrom(),
                jobOffer.getCoin(),
                jobOffer.getLocation(),
                jobOffer.getWorkday(),
                jobOffer.getDescription(),
                jobOffer.getState(),
                jobOffer.getWorkplaceSystem(),
                jobOffer.getHowToApply(),
                jobOffer.getCreatedAt(),
                companyResponse,
                tagsResponse);
    }
}
