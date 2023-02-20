package com.fjss23.jobsearch.domain.registration.token;

import java.time.LocalDateTime;
import java.util.Objects;

public class ConfirmationToken {

    private String id;
    private String token;
    private String appUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    public ConfirmationToken() {}

    public ConfirmationToken(
        String token,
        String appUserEmail,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
    ) {
        this.token = token;
        this.appUserEmail = appUserEmail;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = null;
        this.id = null;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
