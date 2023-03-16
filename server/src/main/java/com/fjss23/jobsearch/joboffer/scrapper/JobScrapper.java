package com.fjss23.jobsearch.joboffer.scrapper;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScrapper {

    private static final Logger logger = LoggerFactory.getLogger(JobScrapper.class);

    public void scrap() {
        try {
            String html = Jsoup.connect("https://news.ycombinator.com/").get().html();
        } catch (IOException e) {
            logger.error("Can't get the information - Exception {}", e.getMessage());
        }
    }
}
