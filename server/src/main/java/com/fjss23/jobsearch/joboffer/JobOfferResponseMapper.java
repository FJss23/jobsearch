package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.CompanyResponse;
import com.fjss23.jobsearch.company.CompanyResponseMapper;
import com.fjss23.jobsearch.tag.TagResponse;
import com.fjss23.jobsearch.tag.TagResponseMapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class JobOfferResponseMapper implements Function<JobOffer, JobOfferResponse> {

    private final TagResponseMapper tagResponseMapper;
    private final CompanyResponseMapper companyResponseMapper;

    public JobOfferResponseMapper(TagResponseMapper tagResponseMapper, CompanyResponseMapper companyResponseMapper) {
        this.tagResponseMapper = tagResponseMapper;
        this.companyResponseMapper = companyResponseMapper;
    }

    @Override
    public JobOfferResponse apply(JobOffer jobOffer) {
        CompanyResponse companyResponse = companyResponseMapper.apply(jobOffer.getCompany());

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
