package com.fjss23.jobsearch.tag;

import com.fjss23.jobsearch.Auditable;

public class Tag extends Auditable {
    private Long id;
    private String defaultName;
    private String code;

    public Tag() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
