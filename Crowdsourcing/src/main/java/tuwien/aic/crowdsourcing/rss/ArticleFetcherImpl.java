package tuwien.aic.crowdsourcing.rss;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tuwien.aic.crowdsourcing.persistence.ArticleManager;

@Component
public class ArticleFetcherImpl implements ArticleFetcher {

    private Set<String> urls = new HashSet<String>();

    @Autowired
    private ArticleManager articleManager;

    private static final Logger logger = LoggerFactory
            .getLogger(ArticleFetcherImpl.class);

    public static void main(String[] args) {

    }

    @Override
    public void addFeed(String url) {
        urls.add(url);
    }

    @Override
    public Map<String, String> getNewArticles() {
        assert articleManager != null;

        Map<String, String> ret = new HashMap<String, String>();
        for (String url : urls) {
            try {
                URL feedSource = new URL(url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedSource));
                @SuppressWarnings("unchecked")
                List<SyndEntry> entries = feed.getEntries();
                for (SyndEntry entry : entries) {
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
