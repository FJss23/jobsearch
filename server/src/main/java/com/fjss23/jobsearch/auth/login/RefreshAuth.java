package com.fjss23.jobsearch.auth.login;

public class RefreshAuth {
    private Long id;
    private String location;
    private String device;
    private String appUserEmail;

    public RefreshAuth(String location, String device, String appUserEmail) {
        this.location = location;
        this.device = device;
        this.appUserEmail = appUserEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAppUserEmail() {
        return appUserEmail;
    }

    public void setAppUserEmail(String appUserEmail) {
        this.appUserEmail = appUserEmail;
    }
}
