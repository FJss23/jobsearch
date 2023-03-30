package com.fjss23.jobsearch.scrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fjss23.jobsearch.job.JobWorkday;
import com.fjss23.jobsearch.job.JobWorkModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/*
* Make this faster (but not a priority).
* TODO: Manage tag (search for technologies, probably add a list of tags in the db)
*/
@Service
public class JobScrappingService {

    private static final Logger logger = LoggerFactory.getLogger(JobScrappingService.class);

    private static final Pattern pEuOrUk = Pattern.compile("EU|\\s[Ee]u\\s|\\([Ee]u\\)|[Aa]ustria|[Vv]ienna|[Bb]elgium|[Bb]russels|[Bb]ulgaria|[Ss]ofia|[C]yprus|[Cc]zech\\s[Rr]epublic|[Pp]rague|[Dd]enmark|[Cc]openhagen|[Ee]stonia|[Ff]inland|[Hh]elsinki|[Ff]rance|[Pp]aris|[Gg]ermany|[Bb]erlin|[Gg]reece|[Aa]thens|[Hh]ungary|[Bb]udapest|[Ii]reland|[Dd]ublin|[Ii]taly|Rome|[Ll]atvia|[Rr]iga|[Ll]ithuania|[Vv]ilnius|[Ll]uxembourg|[Mm]alta|[Vv]alletta|[Nn]etherlands|[Aa]msterdam|[Pp]oland|[Ww]arsaw|[Pp]ortugal|[Ll]isbon|[Rr]omania|[Bb]ucharest|[Ss]lovakia|[Bb]ratislava|[Ss]lovenia|[Ll]jubljana|[Ss]pain|[Mm]adrid|[Bb]arcelona|[Ss]weden|[Ss]tockholm|[Cc]roatia|[Zz]agreb|UK|[Uu]k|[Ee]ngland");

    private static final Pattern pWorkDayPartTime = Pattern.compile("PART[\\s|_|\\-]TIME|[Pp]art[\\s|_|\\-][Tt]ime");
    private static final Pattern pWorkDayPerHours = Pattern.compile("PER[\\s|_|\\-]HOUR|[Pp]er[\\s|_|\\-][Hh]our");
    private static final Pattern pWorkDayFullTime = Pattern.compile("FULL[\\s|_|\\-]TIME|[Ff]ull[\\s|_|\\-][Tt]ime");

    private static final Pattern pWorkplaceRemote = Pattern.compile("REMOTE|[Rr]emote");
    private static final Pattern pWorkplaceHybrid = Pattern.compile("HYBRID|[Hh]ybrid");
    private static final Pattern pWorkplaceOnSite = Pattern.compile("ON[\\s|_|\\-]SITE|[Oo]n[\\s|_|\\-][Ss]ite");

    record JobScrapped(
        String companyName,
        String title,
        String location,
        List<JobWorkday> workDay,
        List<JobWorkModel> workplaceSystem,
        String description
    ) { }

    public void scrappingFromHackerNews() {
        var jobsInEuropeAndUk = new ArrayList<JobScrapped>();
        try {
            Document doc = Jsoup.connect("https://news.ycombinator.com/item?id=34983767").get();
            Elements tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");
            for(Element td: tdsIndent0) {
                Element trContent = td.parent();
                Element comment = trContent.select("td.default > div.comment > span.commtext").first();
                Set<String> location = getEuOrUkCountries(comment.text());

                if (!location.isEmpty()) {
                    /* It is an EU country (or UK) */
                    String headLine = comment.ownText();

                    String company = headLine.split("\\|")[0].trim();
                    String title = headLine;
                    var wkdays = getWorkDay(headLine);
                    var wpsystems = getWorkplaceSystem(headLine);

                    Elements content = comment.children();
                    String description = content.text();

                    var job = new JobScrapped(company, title, location.toString(), wkdays, wpsystems, description);

                    jobsInEuropeAndUk.add(job);
                }
            }
            logger.info("Offer in EU and UK \n: {}", jobsInEuropeAndUk.get(0));
        } catch (IOException e) {
            logger.error("Can't get the information - Exception {}", e.getMessage());
        }
    }

    private List<JobWorkModel> getWorkplaceSystem(String headLine) {
        List<JobWorkModel> workplaceSystems = new ArrayList<>();
        Matcher remote = pWorkplaceRemote.matcher(headLine);
        if (remote.find()) {
            workplaceSystems.add(JobWorkModel.REMOTE);
        }
        Matcher hybrid = pWorkplaceHybrid.matcher(headLine);
        if (hybrid.find()) {
            workplaceSystems.add(JobWorkModel.HYBRID);
        }
        Matcher onSite = pWorkplaceOnSite.matcher(headLine);
        if (onSite.find()) {
            workplaceSystems.add(JobWorkModel.ON_SITE);
        }

        if (workplaceSystems.isEmpty()) workplaceSystems.add(JobWorkModel.ON_SITE);
        return workplaceSystems;
    }

    private List<JobWorkday> getWorkDay(String headLine) {
        List<JobWorkday> workdays = new ArrayList<>();
        Matcher partTime = pWorkDayPartTime.matcher(headLine);
        if (partTime.find()) {
            workdays.add(JobWorkday.PART_TIME);
        }
        Matcher perHours = pWorkDayPerHours.matcher(headLine);
        if (perHours.find()) {
            workdays.add(JobWorkday.PER_HOURS);
        }
        Matcher fullTime = pWorkDayFullTime.matcher(headLine);
        if (fullTime.find()) {
            workdays.add(JobWorkday.FULL_TIME);
        }

        if (workdays.isEmpty()) workdays.add(JobWorkday.FULL_TIME);
        return workdays;
    }

    public Set<String> getEuOrUkCountries(String text) {
        Matcher match = pEuOrUk.matcher(text);
        Set locations = new HashSet();

        while (match.find()) {
            locations.add(match.group());
        }
        return locations;
    }
}
