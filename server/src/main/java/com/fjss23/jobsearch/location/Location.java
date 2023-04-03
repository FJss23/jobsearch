package com.fjss23.jobsearch.location;

import com.fjss23.jobsearch.Auditable;

public class Location extends Auditable {
    private Long id;
    private String name;

    public Location() {}

    public Location(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
