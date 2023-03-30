package com.fjss23.jobsearch.tag;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getTagsOfJob(Long jobId) {
        return tagRepository.getTagsOfJob(jobId);
    }

    public void deleteTagsOfJob(Long jobId) {
        tagRepository.deleteTagsOfJob(jobId);
    }

    public void createTagsOfJob(Long tagId, Long jobId) {
        tagRepository.createTagsOfJob(tagId, jobId);
    }
}
