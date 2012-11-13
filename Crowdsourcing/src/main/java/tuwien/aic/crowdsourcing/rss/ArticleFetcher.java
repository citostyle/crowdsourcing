package tuwien.aic.crowdsourcing.rss;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tuwien.aic.crowdsourcing.persistence.ArticleManager;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component
public class ArticleFetcher {

    private Map<String, List<String>> urls =
            new HashMap<String, List<String>>();

    @Autowired
    private ArticleManager articleManager;

    private static final Logger logger = LoggerFactory
            .getLogger(ArticleFetcher.class);

    public static void main(String[] args) {

    }

    public void addFeed(String url, List<String> toIgnore) {
        urls.put(url, toIgnore);
    }

    public Map<String, String> getNewArticles() {
        Map<String, String> ret = new HashMap<String, String>();
        for (Entry<String, List<String>> entry : urls.entrySet()) {
            try {
                String url = entry.getKey();
                URL feedSource = new URL(url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedSource));
                @SuppressWarnings("unchecked")
                List<SyndEntry> entries = feed.getEntries();
                for (SyndEntry synd : entries) {
                    System.out.println(articleManager.findByAddress(synd
                            .getUri()));
                    if (articleManager.findByAddress(synd.getUri()) == null) {
                        ret.put(synd.getUri(), synd.getTitle());
                    }
                }
            } catch (Exception e) {
                logger.error("Error while trying to fetch feed", e);
            }
        }
        return ret;
    }

    public Map<String, String> getOldArticles() {
        Map<String, String> ret = new HashMap<String, String>();
        for (Entry<String, List<String>> entry : urls.entrySet()) {
            try {
                String url = entry.getKey();
                URL feedSource = new URL(url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedSource));
                @SuppressWarnings("unchecked")
                List<SyndEntry> entries = feed.getEntries();
                for (SyndEntry synd : entries) {
                    System.out.println(articleManager.findByAddress(synd
                            .getUri()));
                    if (articleManager.findByAddress(synd.getUri()) != null) {
                        ret.put(synd.getUri(), synd.getTitle());
                    }
                }
            } catch (Exception e) {
                logger.error("Error while trying to fetch feed", e);
            }
        }
        return ret;
    }
}
