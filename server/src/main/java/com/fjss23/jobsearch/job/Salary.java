package com.fjss23.jobsearch.job;

public class Salary {
    private Double from;
    private Double upTo;
    private String currency;

    public Salary() {}

    public Salary(Double from, Double upTo, String currency) {
        this.from = from;
        this.upTo = upTo;
        this.currency = currency;
    }

    public Double getFrom() {
        return from;
    }

    public void setFrom(Double from) {
        this.from = from;
    }

    public Double getUpTo() {
        return upTo;
    }

    public void setUpTo(Double upTo) {
        this.upTo = upTo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
