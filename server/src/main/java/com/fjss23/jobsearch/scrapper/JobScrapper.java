package com.fjss23.jobsearch.scrapper;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScrapper {

    private static final Logger logger = LoggerFactory.getLogger(JobScrapper.class);

    public void scrap() {
        try {
            Document html = Jsoup.connect("https://news.ycombinator.com/item?id=34983767").get();

        } catch (IOException e) {
            logger.error("Can't get the information - Exception {}", e.getMessage());
        }
    }
}
