package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.Auditable;
import com.fjss23.jobsearch.tag.Tag;
import java.util.Set;

public class Job extends Auditable {
    private Long id;
    private String title;
    private String role;
    private Double salaryFrom;
    private Double salaryUpTo;
    private String salaryCurrency;
    private String location;
    private String workday;
    private String description;
    private JobState state;
    private String workModel;
    private String companyName;
    private String scrappedFromUrl;

    private Set<Tag> tags;

    public Job() {}

    public Job(
            String title,
            String role,
            Double salaryFrom,
            Double salaryUpTo,
            String salaryCurrency,
            String location,
            String workday,
            String description,
            String workModel,
            String companyName,
            Set<Tag> tags) {
        this.title = title;
        this.role = role;
        this.salaryFrom = salaryFrom;
        this.salaryUpTo = salaryUpTo;
        this.salaryCurrency = salaryCurrency;
        this.location = location;
        this.workday = workday;
        this.description = description;
        this.workModel = workModel;
        this.companyName = companyName;
        this.tags = tags;
    }

    public Job(
            String title,
            String location,
            String workday,
            String description,
            String workModel,
            String companyName,
            String scrappedFromUrl,
            Set<Tag> tags) {
        this.title = title;
        this.location = location;
        this.workday = workday;
        this.description = description;
        this.workModel = workModel;
        this.companyName = companyName;
        this.scrappedFromUrl = scrappedFromUrl;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getScrappedFromUrl() {
        return scrappedFromUrl;
    }

    public void setScrappedFromUrl(String scrappedFromUrl) {
        this.scrappedFromUrl = scrappedFromUrl;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getWorkModel() {
        return workModel;
    }

    public void setWorkModel(String workModel) {
        this.workModel = workModel;
    }
}
