package com.fjss23.jobsearch.user;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AppUserRole {
    CANDIDATE("ROLE_CANDIDATE"),
    COMPANY("ROLE_COMPANY"),
    COMPANY_ADMIN("ROLE_COMPANY_ADMIN"),
    APP_ADMIN("ROLE_APP_ADMIN");

    AppUserRole() {}

    private String text;

    AppUserRole(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
