package com.fjss23.jobsearch.joboffer;

import com.fjss23.jobsearch.Auditable;
import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.joboffer.types.JobOfferState;
import com.fjss23.jobsearch.joboffer.types.JobOfferWorkDay;
import com.fjss23.jobsearch.joboffer.types.JobOfferWorkplaceSystem;
import com.fjss23.jobsearch.tag.Tag;
import java.util.ArrayList;
import java.util.List;

public class JobOffer extends Auditable {
    private Long id;
    private String title;
    private String industry;
    private Double salaryFrom;
    private Double salaryUpTo;
    private String coin;
    private String location;
    private JobOfferWorkDay workday;
    private String description;
    private JobOfferState state;
    private JobOfferWorkplaceSystem workplaceSystem;
    private String howToApply;
    private Boolean scrapped;

    private Company company;
    private List<Tag> tags;

    public JobOffer() {
        this.company = new Company();
        this.tags = new ArrayList<>();
    }

    public JobOffer(
            String title,
            String industry,
            Double salaryFrom,
            Double salaryUpTo,
            String coin,
            String location,
            JobOfferWorkDay workday,
            String description,
            JobOfferWorkplaceSystem workplaceSystem,
            String howToApply,
            Company company,
            List<Tag> tags) {
        this.title = title;
        this.industry = industry;
        this.salaryFrom = salaryFrom;
        this.salaryUpTo = salaryUpTo;
        this.coin = coin;
        this.location = location;
        this.workday = workday;
        this.description = description;
        this.workplaceSystem = workplaceSystem;
        this.howToApply = howToApply;
        this.company = company;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Double getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Double salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public Double getSalaryUpTo() {
        return salaryUpTo;
    }

    public void setSalaryUpTo(Double salaryUpTo) {
        this.salaryUpTo = salaryUpTo;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobOfferWorkDay getWorkday() {
        return workday;
    }

    public void setWorkday(JobOfferWorkDay workday) {
        this.workday = workday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobOfferState getState() {
        return state;
    }

    public void setState(JobOfferState state) {
        this.state = state;
    }

    public JobOfferWorkplaceSystem getWorkplaceSystem() {
        return workplaceSystem;
    }

    public void setWorkplaceSystem(JobOfferWorkplaceSystem workplaceSystem) {
        this.workplaceSystem = workplaceSystem;
    }

    public String getHowToApply() {
        return howToApply;
    }

    public void setHowToApply(String howToApply) {
        this.howToApply = howToApply;
    }

    public Boolean getScrapped() {
        return scrapped;
    }

    public void setScrapped(Boolean scrapped) {
        this.scrapped = scrapped;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setCompanyId(Long id) {
        this.getCompany().setId(id);
    }
}
