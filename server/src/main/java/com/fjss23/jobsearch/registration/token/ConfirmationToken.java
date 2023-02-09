package com.fjss23.jobsearch.registration.token;

import java.time.LocalDateTime;
import java.util.Objects;

public class ConfirmationToken {

    private Long id;
    private String token;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
    LocalDateTime confirmedAt;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationToken that = (ConfirmationToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token) && Objects.equals(createdAt, that.createdAt) && Objects.equals(expiresAt, that.expiresAt) && Objects.equals(confirmedAt, that.confirmedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, expiresAt, confirmedAt);
    }
}
