package com.fjss23.jobsearch.tag;

import java.util.function.Function;

public class TagRequestMapper implements Function<TagRequest, Tag> {

    @Override
    public Tag apply(TagRequest tagRequest) {
        return new Tag(tagRequest.id());
    }
}
