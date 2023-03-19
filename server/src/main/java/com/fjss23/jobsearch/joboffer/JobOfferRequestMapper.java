package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagRequestMapper;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class JobOfferRequestMapper implements Function<JobOfferRequest, JobOffer> {

    private final TagRequestMapper tagRequestMapper;

    public JobOfferRequestMapper(TagRequestMapper tagRequestMapper) {
        this.tagRequestMapper = tagRequestMapper;
    }

    @Override
    public JobOffer apply(JobOfferRequest jobOfferRequest) {
        List<Tag> tags = jobOfferRequest.tags().stream().map(tagRequestMapper).collect(Collectors.toList());
        var company = new Company();
        return new JobOffer(
                jobOfferRequest.title(),
                jobOfferRequest.industry(),
                jobOfferRequest.salaryFrom(),
                jobOfferRequest.salaryUpTo(),
                jobOfferRequest.coin(),
                jobOfferRequest.location(),
                jobOfferRequest.workday(),
                jobOfferRequest.description(),
                jobOfferRequest.workplaceSystem(),
                jobOfferRequest.howToApply(),
                company,
                tags);
    }
}
