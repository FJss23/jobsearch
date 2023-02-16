package com.fjss23.jobsearch.domain.registration.token;

import java.time.LocalDateTime;
import java.util.Objects;

public class ConfirmationToken {

    private String token;
    private String email;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
    LocalDateTime confirmedAt;

    public ConfirmationToken() {}

    public ConfirmationToken(
        String token,
        String email,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
    ) {
        this.token = token;
        this.email = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
