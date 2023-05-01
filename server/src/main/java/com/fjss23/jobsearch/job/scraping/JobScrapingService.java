package com.fjss23.jobsearch.job.scraping;

import com.fjss23.jobsearch.job.*;
import com.fjss23.jobsearch.location.Location;
import com.fjss23.jobsearch.location.LocationRepository;
import com.fjss23.jobsearch.tag.Tag;
import com.fjss23.jobsearch.tag.TagService;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormatSymbols;
import java.time.OffsetDateTime;
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
 * (More information about how cron expressions are implemented:
 *      https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions)
 */
@Service
@EnableScheduling
public class JobScrapingService {

    private final JobService jobService;
    private final TagService tagService;
    private final LocationRepository locationRepository;
    private final JobScrapingRepository jobScrapingRepository;

    private final Pattern pWorkdayPartTime = Pattern.compile("PART[\\s|_|\\-]TIME|[Pp]art[\\s|_|\\-][Tt]ime");
    private final Pattern pWorkdayPerHours = Pattern.compile("PER[\\s|_|\\-]HOUR|[Pp]er[\\s|_|\\-][Hh]our");
    private final Pattern pWorkdayFullTime = Pattern.compile("FULL[\\s|_|\\-]TIME|[Ff]ull[\\s|_|\\-][Tt]ime");

    private final Pattern pWorkModelRemote = Pattern.compile("REMOTE|[Rr]emote");
    private final Pattern pWorkModelHybrid = Pattern.compile("HYBRID|[Hh]ybrid");
    private final Pattern pWorkModelOnSite =
            Pattern.compile("ON[\\s|_|\\-]SITE|ONSITE|[Oo]n[\\s|_|\\-][Ss]ite|[Oo]n[Ss]ite");

    private static final String HACKER_NEWS = "HACKER_NEWS";

    private static final Logger logger = LoggerFactory.getLogger(JobScrapingService.class);

    public JobScrapingService(
            JobService jobService,
            TagService tagService,
            LocationRepository locationRepository,
            JobScrapingRepository jobScrapingRepository) {
        this.jobService = jobService;
        this.tagService = tagService;
        this.locationRepository = locationRepository;
        this.jobScrapingRepository = jobScrapingRepository;
    }

    /*
     * 4.00 PM (16:00) every 12 hours on the first 5 days of the month
     */
    @Scheduled(cron = "0 0 16/12 1-5 * ?", zone = "Europe/Madrid")
    public void scrappingUrlFromHackerNews() {
        final String baseUrl = "https://news.ycombinator.com";
        try {
            Document doc = Jsoup.connect(baseUrl + "/ask").get();
            Elements possibleAnchors = doc.select("tr.athing > td.title > span.titleline > a");
            Element found = null;
            var now = OffsetDateTime.now();
            int month = now.getMonth().getValue() - 1;
            int year = now.getYear();
            String monthSymbol = DateFormatSymbols.getInstance().getMonths()[month];
            for (Element element : possibleAnchors) {
                var title = "Ask HN: Who is hiring? (" + monthSymbol + " " + year + ")";
                if (title.contains(element.text())) {
                    found = element;
                    break;
                }
            }

            if (found == null) return;

            String partialPath = found.attr("href");
            var ss = new ScrapingSource(HACKER_NEWS, baseUrl, baseUrl + "/" + partialPath, true);

            jobScrapingRepository.setInactiveBySourceName(HACKER_NEWS);
            jobScrapingRepository.save(ss);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* The post "Ask HN: Who is hiring? (<month> <year>) is created the 1st of each month, at 15.00 +/- 00.05 (my time).
     * If the date is on the weekend, it will be moved to Monday. Companies keep posting new jobs even some days latter.
     * Right now, we are only executing this function in a fixed day (only once).
     *
     * 4.00 PM (16:00) every 12 hours on the first 15 days of the month
     */
    @Scheduled(cron = "0 0 16/12 1-15 * ?", zone = "Europe/Madrid")
    public void scrapingFromHackerNews() {
        final var jobsInEuropeAndUk = new ArrayList<Job>();
        Optional<ScrapingSource> ss = jobScrapingRepository.getScrapingSourceActiveByName(HACKER_NEWS);
        if (ss.isEmpty()) return;

        final String baseUrl = ss.get().getBaseUrl();
        final String url = ss.get().getUrl();

        List<Location> allLocations = locationRepository.findAll();
        StringBuilder regLocations = getRegexLocations(allLocations);
        Pattern pEuropeOrUk = Pattern.compile(regLocations.toString());

        List<Tag> allTags = tagService.findAll();
        StringBuilder regWithAllTags = getRegexTags(allTags);
        Pattern pTags = Pattern.compile(regWithAllTags.toString());

        try {
            var jobs = new ArrayList<JobElement>();
            // Document doc = Jsoup.connect(BASE_URL + "/item?id=34983767").get();
            Document doc = Jsoup.connect(url).get();

            // If we specify the indentation 0, we can get the jobs, avoiding the comments
            Elements tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");

            // A HN post can have multiple pages, lets get the link to the next
            Element moreComments = doc.select(".morelink").first();
            String pathNextPage = moreComments != null ? moreComments.attr("href") : "";

            addJobElement(tdsIndent0, jobs);

            while (!"".equals(pathNextPage)) {
                doc = Jsoup.connect(baseUrl + "/" + pathNextPage).get();
                tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");

                moreComments = doc.select(".morelink").first();
                pathNextPage = moreComments != null ? moreComments.attr("href") : "";

                addJobElement(tdsIndent0, jobs);
            }

            for (JobElement jobElement : jobs) {
                Element trContent = jobElement.element().parent();
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
                    ProcessingJob data = new ProcessingJob(
                            ss.get(),
                            comment.ownText(),
                            comment.children().text(),
                            jobLocation,
                            pTags,
                            allTags,
                            jobElement.idFromSource());

                    Job job = processJob(data);

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
                    logger.error("Error trying to save a job {} - {}", job.getTitle(), e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Can't get the information from {} - Exception {}", url, e.getMessage());
        }
    }

    private Job processJob(ProcessingJob data) {
        String locations = processLocation(data.location());

        String companyName = data.title().split("\\|")[0].trim();

        var processedCompanyName = companyName.split(" ");
        if (processedCompanyName.length > 3) {
            companyName = processedCompanyName[0] + processedCompanyName[1];
        }

        String workday = getWorkDayEnums(data.title()).stream()
                .map(jobWorkday -> jobWorkday.name() + " ")
                .collect(Collectors.joining())
                .trim();

        String workModel = getWorkModelEnums(data.title()).stream()
                .map(jobWorkModel -> jobWorkModel.name() + " ")
                .collect(Collectors.joining())
                .trim();

        var searchForTags = data.title() + "\n" + data.description();
        Set<Tag> jobTags = processTags(searchForTags, data.pTags(), data.tags());

        String companyLogoUrl = "https://ui-avatars.com/api/?name=" + companyName;

        var company = new Company(companyName, companyLogoUrl);

        var job = new Job(
                data.title(),
                locations,
                workday,
                data.description(),
                workModel,
                company,
                data.idFromSource(),
                data.ss(),
                jobTags);

        return job;
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

    private void addJobElement(Elements tds, List<JobElement> jobs) {
        for (Element td : tds) {
            Element parent = td.parent();
            Elements anchor = parent.select("span.age > a");
            String idFromSource = anchor.first().attr("href");
            jobs.add(new JobElement(idFromSource, td));
        }
    }

    record JobElement(String idFromSource, Element element) {}

    record ProcessingJob(
            ScrapingSource ss,
            String title,
            String description,
            List<String> location,
            Pattern pTags,
            List<Tag> tags,
            String idFromSource) {}
}
