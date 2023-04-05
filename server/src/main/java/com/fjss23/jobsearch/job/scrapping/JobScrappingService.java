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

    private final Pattern pWorkdayPartTime = Pattern.compile("PART[\\s|_|\\-]TIME|[Pp]art[\\s|_|\\-][Tt]ime");
    private final Pattern pWorkdayPerHours = Pattern.compile("PER[\\s|_|\\-]HOUR|[Pp]er[\\s|_|\\-][Hh]our");
    private final Pattern pWorkdayFullTime = Pattern.compile("FULL[\\s|_|\\-]TIME|[Ff]ull[\\s|_|\\-][Tt]ime");

    private final Pattern pWorkModelRemote = Pattern.compile("REMOTE|[Rr]emote");
    private final Pattern pWorkModelHybrid = Pattern.compile("HYBRID|[Hh]ybrid");
    private final Pattern pWorkModelOnSite =
            Pattern.compile("ON[\\s|_|\\-]SITE|ONSITE|[Oo]n[\\s|_|\\-][Ss]ite|[Oo]n[Ss]ite");

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
        final var jobsInEuropeAndUk = new ArrayList<Job>();
        final String BASE_URL = "https://news.ycombinator.com";

        List<Location> allLocations = locationRepository.findAll();
        StringBuilder regLocations = getRegexLocations(allLocations);
        Pattern pEuropeOrUk = Pattern.compile(regLocations.toString());

        List<Tag> allTags = tagService.findAll();
        StringBuilder regWithAllTags = getRegexTags(allTags);
        Pattern pTags = Pattern.compile(regWithAllTags.toString());

        try {
            final Elements possibleJobs = new Elements();
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

                Matcher matchEuOrUk = pEuropeOrUk.matcher(comment.text());
                List<String> jobLocation = new ArrayList<>();

                while (matchEuOrUk.find()) {
                    jobLocation.add(matchEuOrUk.group());
                }

                if (!jobLocation.isEmpty()) {
                    // It is an EU country (or UK)
                    Job job = processJob(
                            comment.baseUri(),
                            comment.ownText(),
                            comment.children().text(),
                            jobLocation,
                            pTags,
                            allTags);
                    jobsInEuropeAndUk.add(job);
                }
            }

            // From 100 to 200 jobs are inserted (on avg), saving the new entries one by one is a valid solution for
            // this number of jobs. If more inserts are expected, a more performant solution could be explored,
            // probably a saveAll function that calls batchUpdate, so jobs are inserted in big chunks.
            for (Job job : jobsInEuropeAndUk) {
                try {
                    jobService.save(job);
                } catch (Exception e) {
                    logger.error(
                            "Error trying to save the job {} - Exception {}", job.getDescription(), e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Can't get the information from {} - Exception {}", BASE_URL, e.getMessage());
        }
    }

    private Job processJob(
            String uri, String title, String description, List<String> jobLocation, Pattern pTags, List<Tag> allTags) {
        String locations = processLocation(jobLocation);

        String company = title.split("\\|")[0].trim();

        String workday = getWorkDayEnums(title).stream()
                .map(jobWorkday -> jobWorkday.name() + " ")
                .collect(Collectors.joining())
                .trim();

        String workModel = getWorkModelEnums(title).stream()
                .map(jobWorkModel -> jobWorkModel.name() + " ")
                .collect(Collectors.joining())
                .trim();

        Set<Tag> jobTags = processTags(description, pTags, allTags);

        String companyLogoUrl = "https://ui-avatars.com/api/?name=" + company;

        return new Job(title, locations, workday, description, workModel, company, uri, companyLogoUrl, jobTags);
    }

    private Set<Tag> processTags(String description, Pattern pTags, List<Tag> allTags) {
        StringBuilder tagsFound = new StringBuilder();
        Matcher matchTags = pTags.matcher(description.toLowerCase());

        while (matchTags.find()) {
            tagsFound.append(matchTags.group()).append(",");
        }

        String[] splitTags = tagsFound.toString().replaceAll("\\W", " ").split(" ");

        List<String> processedTags = Arrays.stream(splitTags)
                .map(String::trim)
                .filter(tag -> !"".equals(tag))
                .toList();

        return allTags.stream()
                .filter(tag -> processedTags.contains(tag.getName().toLowerCase()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private static String processLocation(List<String> jobLocation) {
        Set<String> uniqueLocations = new HashSet<>();
        for (String location : jobLocation) {
            String locationWithoutNoise = location.replaceAll("\\W", "");
            uniqueLocations.add(locationWithoutNoise);
        }

        StringBuilder processedLocation = new StringBuilder();
        uniqueLocations.forEach(location -> processedLocation.append(location).append(" "));

        return processedLocation.toString().trim();
    }

    // TODO: Current behaviour -> [Cc]zech Republic. Desired behaviour -> [Cc]zech [Rr]epublic
    private StringBuilder getRegexLocations(List<Location> allLocations) {
        StringBuilder regLocations = new StringBuilder();
        regLocations.append("\\W(");
        for (int i = 0; i < allLocations.size(); i++) {
            var name = allLocations.get(i).getName();
            var nameFirstLetter = name.substring(0, 1);
            var nameWithoutFirstLetter = name.substring(1);
            var modifiedName =
                    "[" + nameFirstLetter.toUpperCase() + nameFirstLetter.toLowerCase() + "]" + nameWithoutFirstLetter;
            regLocations.append(modifiedName);
            if (i < allLocations.size() - 1) {
                regLocations.append("|");
            } else {
                regLocations.append(")\\W");
            }
        }
        return regLocations;
    }

    private Set<JobWorkModel> getWorkModelEnums(String headLine) {
        Set<JobWorkModel> workplaceSystems = new HashSet<>();
        Matcher remote = this.pWorkModelRemote.matcher(headLine);
        if (remote.find()) {
            workplaceSystems.add(JobWorkModel.REMOTE);
        }
        Matcher hybrid = this.pWorkModelHybrid.matcher(headLine);
        if (hybrid.find()) {
            workplaceSystems.add(JobWorkModel.HYBRID);
        }
        Matcher onSite = this.pWorkModelOnSite.matcher(headLine);
        if (onSite.find()) {
            workplaceSystems.add(JobWorkModel.ON_SITE);
        }

        if (workplaceSystems.isEmpty()) workplaceSystems.add(JobWorkModel.ON_SITE);
        return workplaceSystems;
    }

    private Set<JobWorkday> getWorkDayEnums(String headLine) {
        Set<JobWorkday> workdays = new HashSet<>();
        Matcher partTime = this.pWorkdayPartTime.matcher(headLine);
        if (partTime.find()) {
            workdays.add(JobWorkday.PART_TIME);
        }
        Matcher perHours = this.pWorkdayPerHours.matcher(headLine);
        if (perHours.find()) {
            workdays.add(JobWorkday.PER_HOURS);
        }
        Matcher fullTime = this.pWorkdayFullTime.matcher(headLine);
        if (fullTime.find()) {
            workdays.add(JobWorkday.FULL_TIME);
        }

        if (workdays.isEmpty()) workdays.add(JobWorkday.FULL_TIME);
        return workdays;
    }

    private StringBuilder getRegexTags(List<Tag> allTags) {
        StringBuilder regTags = new StringBuilder();
        regTags.append("\\W(");
        for (int i = 0; i < allTags.size(); i++) {
            var name = allTags.get(i).getName().toLowerCase();
            if (name.contains(".")) {
                // e.g: .NET
                name = name.replaceAll("\\.", "\\.");
            }
            if (name.contains("+")) {
                // e.g: C++
                name = name.replaceAll("\\+", "\\+");
            }
            regTags.append(name);
            if (i < allTags.size() - 1) {
                regTags.append("|");
            } else {
                regTags.append(")\\W");
            }
        }
        return regTags;
    }
}
