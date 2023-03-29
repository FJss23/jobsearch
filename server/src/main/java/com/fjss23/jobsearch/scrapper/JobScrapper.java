package com.fjss23.jobsearch.scrapper;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScrapper {

    private static final Logger logger = LoggerFactory.getLogger(JobScrapper.class);

    public static void scrap() {
        try {
            Document doc = Jsoup.connect("https://news.ycombinator.com/item?id=34983767").get();
            Elements tdsIndent0 = doc.select("tr.athing > td > table > tbody > tr > td[indent=\"0\"]");
            Elements trs = tdsIndent0.parents();
            for(Element tr: trs) {
                Element comment = tr.select("td.default > div.comment > span.commtext").first();
                String headLine = comment.ownText();
                Elements content = comment.children();
                logger.info("Scrapping the web {}", headLine);
            }
        } catch (IOException e) {
            logger.error("Can't get the information - Exception {}", e.getMessage());
        }
    }
}
