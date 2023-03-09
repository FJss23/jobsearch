package com.fjss23.jobsearch.auth.registration.token;

import java.time.OffsetDateTime;

public class ConfirmationToken {

    private String id;
    private String token;
    private String appUserEmail;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private OffsetDateTime confirmedAt;

    public ConfirmationToken() { }

    public ConfirmationToken(String id, String token, String appUserEmail, OffsetDateTime createdAt, OffsetDateTime expiresAt, OffsetDateTime confirmedAt) {
        this.id = id;
        this.token = token;
        this.appUserEmail = appUserEmail;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = confirmedAt;
    }

    public ConfirmationToken(String token, String email, OffsetDateTime createdAt, OffsetDateTime expiresAt) {
        this.token = token;
        this.appUserEmail = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppUserEmail() {
        return appUserEmail;
    }

    public void setAppUserEmail(String appUserEmail) {
        this.appUserEmail = appUserEmail;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public OffsetDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(OffsetDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
