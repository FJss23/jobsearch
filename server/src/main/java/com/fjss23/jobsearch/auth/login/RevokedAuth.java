package com.fjss23.jobsearch.auth.login;

public class RevokedAuth {
    private Long id;
    private String appUserEmail;

    public RevokedAuth(String appUserEmail) {
        this.appUserEmail = appUserEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppUserEmail() {
        return appUserEmail;
    }

    public void setAppUserEmail(String appUserEmail) {
        this.appUserEmail = appUserEmail;
    }
}
