package com.fjss23.jobsearch.email.ses;

public class EmailSent {
    private Long id;
    private String email;
    private EmailSentStatus status;

    public EmailSent(String email, EmailSentStatus status) {
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmailSentStatus getStatus() {
        return status;
    }

    public void setStatus(EmailSentStatus status) {
        this.status = status;
    }
}
