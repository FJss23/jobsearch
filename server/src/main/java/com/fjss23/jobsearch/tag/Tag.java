package com.fjss23.jobsearch.tag;

import com.fjss23.jobsearch.Auditable;

public class Tag extends Auditable {
    private Long id;
    private String name;

    public Tag() {}

    public Tag(Long id) {
        this.id = id;
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
