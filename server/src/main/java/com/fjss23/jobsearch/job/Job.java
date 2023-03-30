package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.Auditable;
import com.fjss23.jobsearch.company.Company;
import com.fjss23.jobsearch.tag.Tag;
import java.util.ArrayList;
import java.util.List;

public class Job extends Auditable {
    private Long id;
    private String title;
    private String industry;
    private Double salaryFrom;
    private Double salaryUpTo;
    private String coin;
    private String location;
    private JobWorkday workday;
    private String description;
    private JobState state;
    private JobWorkModel workModel;
    private String howToApply;
    private Boolean scrapped;

    private Company company;
    private List<Tag> tags;

    public Job() {
        this.company = new Company();
        this.tags = new ArrayList<>();
    }

    public Job(
            String title,
            String industry,
            Double salaryFrom,
            Double salaryUpTo,
            String coin,
            String location,
            JobWorkday workday,
            String description,
            JobWorkModel workModel,
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
        this.workModel = workModel;
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

    public JobWorkday getWorkday() {
        return workday;
    }

    public void setWorkday(JobWorkday workday) {
        this.workday = workday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public JobWorkModel getWorkModel() {
        return workModel;
    }

    public void setWorkModel(JobWorkModel workModel) {
        this.workModel = workModel;
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
