
package tuwien.aic.crowdsourcing.rss;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Set<String> urls = new HashSet<String>();

    @Autowired
    private ArticleManager articleManager;

    private static final Logger logger = LoggerFactory
            .getLogger(ArticleFetcher.class);

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
                for (SyndEntry synd : entries) {
                    if (articleManager.findByAddress(synd.getLink()) == null) {
                        ret.put(synd.getLink(), synd.getTitle());
                    }
                }
            } catch (Exception e) {
                logger.error("Error while trying to fetch feed", e);
            }
        }
        return ret;
    }
}
