package com.fjss23.jobsearch.job;

import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagService;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final TagService tagService;

    public JobService(JobRepository jobRepository, TagService tagService) {
        this.jobRepository = jobRepository;
        this.tagService = tagService;
    }

    public List<Job> findAll() {
        List<Job> jobs = jobRepository.findAll();

        for (Job job : jobs) {
            List<Tag> tags = tagService.getTagsOfJob(job.getId());
            job.setTags(new HashSet<>(tags));
        }

        return jobs;
    }

    public Job findById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job offer not found"));

        List<Tag> tags = tagService.getTagsOfJob(id);
        job.setTags(new HashSet<>(tags));

        return job;
    }

    @Transactional
    public Long deleteById(Long id) {
        tagService.deleteTagsOfJob(id);
        return jobRepository.deleteById(id);
    }

    @Transactional
    public Job save(Job job) {
        job.setState(JobState.CREATED);
        var createdJob = jobRepository.save(job);
        for (Tag tag : job.getTags()) {
            tagService.createTagsOfJob(tag.getId(), createdJob.getId());
        }
        createdJob.setTags(job.getTags());
        return createdJob;
    }

    @Transactional
    public int[] saveAll(List<Job> jobs) {
        jobs.forEach(job -> job.setState(JobState.CREATED));
        var createdJobs = jobRepository.saveAll(jobs);
        /*for (Job job: jobs) {
            for (Tag tag : job.getTags()) {
                tagService.createTagsOfJob(tag.getId(), createdJob.getId());
            }
            createdJob.setTags(job.getTags());
        }*/
        try {
            throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // return createdJobs;
    }
}