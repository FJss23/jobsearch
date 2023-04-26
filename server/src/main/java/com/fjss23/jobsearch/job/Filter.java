package com.fjss23.jobsearch.job;

import java.util.List;

public record Filter(
        String search, List<JobWorkday> workdays, List<JobWorkModel> workModels, Long from, int size, String page) {

    public String[] getTerms() {
        if (search == null) return new String[0];
        String[] terms = search.split("\s");
        return terms;
    }
}
