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
import org.springframework.stereotype.Service;

/*
 * Make this faster (but not a priority).
 * TODO: Why isn't <Madrid> being added?
 * TODO: Get more jobs adding the visa sponsor option
 * TODO: Solve problem with locations (showed in the picture)
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

    // @Scheduled()
    public void scrappingFromHackerNews() {
        var jobsInEuropeAndUk = new ArrayList<Job>();
        try {
            Elements possibleJobs = new Elements();
            Document doc = Jsoup.connect(BASE_URL + "/item?id=34983767").get();
            Elements tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");

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
                    /* It is an EU country (or UK) */
                    String uri = comment.baseUri();

                    Set<String> uniqueLocations = new HashSet<>();
                    for (String location : jobLocation) {
                        String locationWithoutNoise = location.replaceAll("\\W", "");
                        uniqueLocations.add(locationWithoutNoise);
                    }

                    StringBuilder processedLocation = new StringBuilder();
                    uniqueLocations
                            .forEach(location ->
                                    processedLocation.append(location).append(" "));

                    String headLine = comment.ownText();
                    String company = headLine.split("\\|")[0].trim();

                    var wkDays = getWorkDayEnums(headLine).stream()
                            .map(jobWorkday -> jobWorkday.name() + ",")
                            .collect(Collectors.joining());

                    var wkModels = getWorkModelEnums(headLine).stream()
                            .map(jobWorkModel -> jobWorkModel.name() + ",")
                            .collect(Collectors.joining());

                    Elements content = comment.children();
                    String description = content.text();

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

                    Set<Tag> jobsTags = getTagsObjects(processedTags, allTags);

                    var job = new Job(
                            headLine,
                            processedLocation.toString().trim(),
                            wkDays,
                            description,
                            wkModels,
                            company,
                            uri,
                            jobsTags);

                    jobsInEuropeAndUk.add(job);
                }
            }

            jobService.save(jobsInEuropeAndUk.get(0));
        } catch (IOException e) {
            logger.error("Can't get the information - Exception {}", e.getMessage());
        }
    }

    private Set<Tag> getTagsObjects(List<String> processedTags, List<Tag> allTags) {
        return allTags.stream()
                .filter(tag -> processedTags.contains(tag.getName().toLowerCase()))
                .collect(Collectors.toUnmodifiableSet());
    }

    // Current behaviour -> [Cc]zech Republic. Desired behaviour -> [Cc]zech [Rr]epublic
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
                /* .NET */
                name = name.replaceAll("\\.", "\\.");
            }
            if (name.contains("+")) {
                /* C++ */
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
