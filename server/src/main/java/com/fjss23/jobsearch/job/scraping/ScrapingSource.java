package com.fjss23.jobsearch.job.scraping;

import com.fjss23.jobsearch.Auditable;

public class ScrapingSource extends Auditable {
    private Long id;
    private String name;
    private String baseUrl;
    private String url;
    private Boolean active;

    public ScrapingSource() {}

    public ScrapingSource(Long id, String name, String baseUrl, String url, Boolean active) {
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
        this.url = url;
        this.active = active;
    }

    public ScrapingSource(String name, String baseUrl, String url, Boolean active) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.url = url;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
