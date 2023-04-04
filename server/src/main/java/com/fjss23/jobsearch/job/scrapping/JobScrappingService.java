package com.fjss23.jobsearch.job.scrapping;

import com.fjss23.jobsearch.job.Job;
import com.fjss23.jobsearch.job.JobService;
import com.fjss23.jobsearch.job.JobWorkModel;
import com.fjss23.jobsearch.job.JobWorkday;
import com.fjss23.jobsearch.location.Location;
import com.fjss23.jobsearch.location.LocationRepository;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagService;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
 * TODO: Get more jobs adding the visa sponsor option
 */
@Service
@EnableScheduling
public class JobScrappingService {

    private final JobService jobService;
    private final TagService tagService;
    private final LocationRepository locationRepository;

    private static final Pattern pWorkdayPartTime = Pattern.compile("PART[\\s|_|\\-]TIME|[Pp]art[\\s|_|\\-][Tt]ime");
    private static final Pattern pWorkdayPerHours = Pattern.compile("PER[\\s|_|\\-]HOUR|[Pp]er[\\s|_|\\-][Hh]our");
    private static final Pattern pWorkdayFullTime = Pattern.compile("FULL[\\s|_|\\-]TIME|[Ff]ull[\\s|_|\\-][Tt]ime");

    private static final Pattern pWorkModelRemote = Pattern.compile("REMOTE|[Rr]emote");
    private static final Pattern pWorkModelHybrid = Pattern.compile("HYBRID|[Hh]ybrid");
    private static final Pattern pWorkModelOnSite =
            Pattern.compile("ON[\\s|_|\\-]SITE|ONSITE|[Oo]n[\\s|_|\\-][Ss]ite|[Oo]n[Ss]ite");

    private final String BASE_URL = "https://news.ycombinator.com";

    private static final Logger logger = LoggerFactory.getLogger(JobScrappingService.class);

    public JobScrappingService(JobService jobService, TagService tagService, LocationRepository locationRepository) {
        this.jobService = jobService;
        this.tagService = tagService;
        this.locationRepository = locationRepository;
    }

    // The post "Ask HN: Who is hiring? (<month> <year>) is created the 1st of each month, at 15.00 +/- 00.05 (my time).
    // If the date is on the weekend, it will be moved to Monday. Companies keep posting new jobs even some days latter.
    // Right now, we are only executing this function in a fixed day (only once).
    // TODO: Run it every day after the 1st of <month>, to add jobs that are posted later, this will require to check
    //       if a job is already stored in the database or not.
    // (More information about how cron expressions are implemented:
    //      https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions)
    @Scheduled(cron = "0 0 20 3 * ?", zone = "Europe/Madrid")
    public void scrappingFromHackerNews() {
        var jobsInEuropeAndUk = new ArrayList<Job>();
        try {
            Elements possibleJobs = new Elements();
            // TODO: Search by the name Ask HN: Who is hiring? (<month> <year>) and get the content
            Document doc = Jsoup.connect(BASE_URL + "/item?id=34983767").get();
            Elements tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");

            // A HN post can have multiple pages, lets get the link to the next
            Element moreComments = doc.select(".morelink").first();
            String pathNextPage = moreComments != null ? moreComments.attr("href") : "";

            possibleJobs.addAll(tdsIndent0);

            while (!"".equals(pathNextPage)) {
                doc = Jsoup.connect(BASE_URL + "/" + pathNextPage).get();
                tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");

                moreComments = doc.select(".morelink").first();
                pathNextPage = moreComments != null ? moreComments.attr("href") : "";

                possibleJobs.addAll(tdsIndent0);
            }

            List<Location> listOfLocations = locationRepository.findAll();
            StringBuilder regLocations = getRegexLocations(listOfLocations);

            Pattern pEuOrUk = Pattern.compile(regLocations.toString());

            List<Tag> allTags = tagService.findAll();
            StringBuilder regWithAllTags = getRegexTags(allTags);

            Pattern pTags = Pattern.compile(regWithAllTags.toString());

            for (Element td : possibleJobs) {
                Element trContent = td.parent();
                Element comment = trContent
                        .select("td.default > div.comment > span.commtext")
                        .first();

                // Sometimes posts are flagged or deleted
                if (comment == null) {
                    logger.info("Couldn't extract the information from the following comment: {}", trContent);
                    continue;
                }

                Matcher matchEuOrUk = pEuOrUk.matcher(comment.text());
                List<String> jobLocation = new ArrayList<>();

                while (matchEuOrUk.find()) {
                    jobLocation.add(matchEuOrUk.group());
                }

                if (!jobLocation.isEmpty()) {
                    // It is an EU country (or UK)
                    String uri = comment.baseUri();

                    // Processing the locations of the job
                    Set<String> uniqueLocations = new HashSet<>();
                    for (String location : jobLocation) {
                        String locationWithoutNoise = location.replaceAll("\\W", "");
                        uniqueLocations.add(locationWithoutNoise);
                    }

                    StringBuilder processedLocation = new StringBuilder();
                    uniqueLocations.forEach(
                            location -> processedLocation.append(location).append(" "));

                    String locations = processedLocation.toString().trim();

                    // Processing the title
                    String title = comment.ownText();

                    // Processing the name of the company
                    String company = title.split("\\|")[0].trim();

                    // Processing the workday. e.g: "FULL_TIME PART_TIME"
                    String workday = getWorkDayEnums(title).stream()
                            .map(jobWorkday -> jobWorkday.name() + " ")
                            .collect(Collectors.joining())
                            .trim();

                    // Processing the work model. e.g: "REMOTE HYBRID"
                    String workModel = getWorkModelEnums(title).stream()
                            .map(jobWorkModel -> jobWorkModel.name() + " ")
                            .collect(Collectors.joining())
                            .trim();

                    // Processing the description
                    Elements content = comment.children();
                    String description = content.text();

                    // Processing the tags
                    StringBuilder tagsFound = new StringBuilder();
                    Matcher matchTags = pTags.matcher(description.toLowerCase());

                    while (matchTags.find()) {
                        tagsFound.append(matchTags.group()).append(",");
                    }

                    String[] splitTags =
                            tagsFound.toString().replaceAll("\\W", "").split(",");

                    List<String> processedTags = Arrays.stream(splitTags)
                            .map(String::trim)
                            .filter(tag -> !"".equals(tag))
                            .toList();

                    Set<Tag> jobTags = getTagsObjects(processedTags, allTags);

                    var job = new Job(title, locations, workday, description, workModel, company, uri, jobTags);

                    jobsInEuropeAndUk.add(job);
                }
            }

            // From 100 to 200 jobs are inserted (on avg), saving the new entries one by one is a valid solution for
            // this number of jobs. If more inserts are expected, a more performant solution could be explored,
            // probably a saveAll function that calls batchUpdate, so jobs are inserted in big chunks.
            for (Job job : jobsInEuropeAndUk) {
                try {
                    jobService.save(job);
                } catch(Exception e) {
                    logger.error("Error trying to save the job {} - Exception {}", job.getDescription(), e.getMessage());
                    continue;
                }
            }
        } catch (IOException e) {
            logger.error("Can't get the information from {} - Exception {}", BASE_URL, e.getMessage());
        }
    }

    private Set<Tag> getTagsObjects(List<String> processedTags, List<Tag> allTags) {
        return allTags.stream()
                .filter(tag -> processedTags.contains(tag.getName().toLowerCase()))
                .collect(Collectors.toUnmodifiableSet());
    }

    // TODO: Current behaviour -> [Cc]zech Republic. Desired behaviour -> [Cc]zech [Rr]epublic
    private StringBuilder getRegexLocations(List<Location> listOfLocations) {
        StringBuilder regLocations = new StringBuilder();
        regLocations.append("\\W(");
        for (int i = 0; i < listOfLocations.size(); i++) {
            var name = listOfLocations.get(i).getName();
            var nameFirstLetter = name.substring(0, 1);
            var nameWithoutFirstLetter = name.substring(1);
            var modifiedName =
                    "[" + nameFirstLetter.toUpperCase() + nameFirstLetter.toLowerCase() + "]" + nameWithoutFirstLetter;
            regLocations.append(modifiedName);
            if (i < listOfLocations.size() - 1) {
                regLocations.append("|");
            } else {
                regLocations.append(")\\W");
            }
        }
        return regLocations;
    }

    private Set<JobWorkModel> getWorkModelEnums(String headLine) {
        Set<JobWorkModel> workplaceSystems = new HashSet<>();
        Matcher remote = pWorkModelRemote.matcher(headLine);
        if (remote.find()) {
            workplaceSystems.add(JobWorkModel.REMOTE);
        }
        Matcher hybrid = pWorkModelHybrid.matcher(headLine);
        if (hybrid.find()) {
            workplaceSystems.add(JobWorkModel.HYBRID);
        }
        Matcher onSite = pWorkModelOnSite.matcher(headLine);
        if (onSite.find()) {
            workplaceSystems.add(JobWorkModel.ON_SITE);
        }

        if (workplaceSystems.isEmpty()) workplaceSystems.add(JobWorkModel.ON_SITE);
        return workplaceSystems;
    }

    private Set<JobWorkday> getWorkDayEnums(String headLine) {
        Set<JobWorkday> workdays = new HashSet<>();
        Matcher partTime = pWorkdayPartTime.matcher(headLine);
        if (partTime.find()) {
            workdays.add(JobWorkday.PART_TIME);
        }
        Matcher perHours = pWorkdayPerHours.matcher(headLine);
        if (perHours.find()) {
            workdays.add(JobWorkday.PER_HOURS);
        }
        Matcher fullTime = pWorkdayFullTime.matcher(headLine);
        if (fullTime.find()) {
            workdays.add(JobWorkday.FULL_TIME);
        }

        if (workdays.isEmpty()) workdays.add(JobWorkday.FULL_TIME);
        return workdays;
    }

    private StringBuilder getRegexTags(List<Tag> tags) {
        StringBuilder regTags = new StringBuilder();
        regTags.append("\\W(");
        for (int i = 0; i < tags.size(); i++) {
            var name = tags.get(i).getName().toLowerCase();
            if (name.contains(".")) {
                // e.g: .NET
                name = name.replaceAll("\\.", "\\.");
            }
            if (name.contains("+")) {
                // e.g: C++
                name = name.replaceAll("\\+", "\\+");
            }
            regTags.append(name);
            if (i < tags.size() - 1) {
                regTags.append("|");
            } else {
                regTags.append(")\\W");
            }
        }
        return regTags;
    }
}
