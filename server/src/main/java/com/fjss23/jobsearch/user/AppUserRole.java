package com.fjss23.jobsearch.user;

public enum AppUserRole {
    CANDIDATE("ROLE_CANDIDATE"),
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
