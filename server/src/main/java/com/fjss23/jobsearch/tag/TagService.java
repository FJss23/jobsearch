package com.fjss23.jobsearch.tag;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getTagsOfJobOffer(Long jobOfferId) {
        return tagRepository.getTagsOfJobOffer(jobOfferId);
    }

    public void deleteTagsOfJobOffer(Long jobOfferId) {
        tagRepository.deleteTagsOfJobOffer(jobOfferId);
    }

    public void createTagsOfJobOffer(Long tagId, Long jobOfferId) {
        tagRepository.createTagsOfJobOffer(tagId, jobOfferId);
    }
}
