package com.fjss23.jobsearch.tag.payload;

import java.util.function.Function;

import com.fjss23.jobsearch.tag.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagRequestMapper implements Function<TagRequest, Tag> {

    @Override
    public Tag apply(TagRequest tagRequest) {
        return new Tag(tagRequest.id());
    }
}
