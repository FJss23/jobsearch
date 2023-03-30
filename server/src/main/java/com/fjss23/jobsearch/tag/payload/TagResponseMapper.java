package com.fjss23.jobsearch.tag.payload;

import java.util.function.Function;

import com.fjss23.jobsearch.tag.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagResponseMapper implements Function<Tag, TagResponse> {

    @Override
    public TagResponse apply(Tag tag) {
        return new TagResponse(tag.getId(), tag.getDefaultName(), tag.getCode());
    }
}
