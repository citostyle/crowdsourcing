package tuwien.aic.crowdsourcing.rss;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tuwien.aic.crowdsourcing.persistence.ArticleManager;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component
public class ArticleFetcher {

    private Set<String> urls = new HashSet<String>();

    @Autowired
    private ArticleManager articleManager;

    private static final Logger logger = Logger.getLogger(ArticleFetcher.class);

    public static void main(String[] args) {

    }

    public void addFeed(String url) {
        urls.add(url);
    }

    public Map<String, String> getNewArticles() {
        Map<String, String> ret = new HashMap<String, String>();
        for (String url : urls) {
            try {
                URL feedSource = new URL(url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedSource));
                @SuppressWarnings("unchecked")
                List<SyndEntry> entries = feed.getEntries();
                for (SyndEntry entry : entries) {
                    System.out.println(articleManager.getArticleByAddress(entry
                            .getUri()));
                    if (articleManager.getArticleByAddress(entry.getUri()) == null) {
                        ret.put(entry.getUri(), entry.getTitle());
                    }
                }
            } catch (Exception e) {
                logger.error("Error while trying to fetch feed", e);
            }
        }
        return ret;
    }
}
