package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.Auditable;
import com.fjss23.jobsearch.job.scraping.ScrapingSource;
import com.fjss23.jobsearch.tag.Tag;
import java.util.Set;

public class Job extends Auditable {
    private Long id;
    private String title;
    private String role;
    private Salary salary = new Salary();
    private String location;
    private String workday;
    private String description;
    private JobState state = JobState.CREATED;
    private String workModel;
    private Company company = new Company();
    private String idFromSource;
    private Boolean visaSponsor;
    private ScrapingSource scrapingSource = new ScrapingSource();

    private Set<Tag> tags;

    public Job() {}

    public Job(
            String title,
            String location,
            String workday,
            String description,
            String workModel,
            Company company,
            String idFromSource,
            ScrapingSource scrapingSource,
            Set<Tag> tags) {
        this.title = title;
        this.role = null;
        this.salary = new Salary();
        this.location = location;
        this.workday = workday;
        this.description = description;
        this.workModel = workModel;
        this.company = company;
        this.visaSponsor = false;
        this.idFromSource = idFromSource;
        this.scrapingSource = scrapingSource;
        this.tags = tags;
    }

    public Job(
            String title,
            String location,
            String workday,
            String description,
            String workModel,
            Salary salary,
            Company company,
            String idFromSource,
            ScrapingSource scrapingSource,
            Set<Tag> tags) {
        this.title = title;
        this.role = null;
        this.salary = salary;
        this.location = location;
        this.workday = workday;
        this.description = description;
        this.workModel = workModel;
        this.company = company;
        this.visaSponsor = false;
        this.idFromSource = idFromSource;
        this.scrapingSource = scrapingSource;
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

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public String getIdFromSource() {
        return idFromSource;
    }

    public void setIdFromSource(String idFromSource) {
        this.idFromSource = idFromSource;
    }

    public Boolean getVisaSponsor() {
        return visaSponsor;
    }

    public void setVisaSponsor(Boolean visaSponsor) {
        this.visaSponsor = visaSponsor;
    }

    public ScrapingSource getScrapingSource() {
        return scrapingSource;
    }

    public void setScrapingSource(ScrapingSource scrapingSource) {
        this.scrapingSource = scrapingSource;
    }

    public void setSalaryUpTo(Double upTo) {
        this.salary.setUpTo(upTo);
    }

    public void setSalaryFrom(Double from) {
        this.salary.setFrom(from);
    }

    public void setSalaryCurrency(String currency) {
        this.salary.setCurrency(currency);
    }

    public void setCompanyName(String name) {
        this.company.setName(name);
    }

    public void setCompanyLogoUrl(String url) {
        this.company.setLogoUrl(url);
    }

    public void setScrapingSourceId(Long id) {
        this.scrapingSource.setId(id);
    }
}
