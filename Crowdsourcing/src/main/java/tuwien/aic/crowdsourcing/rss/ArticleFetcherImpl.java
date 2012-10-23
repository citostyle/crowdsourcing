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

import tuwien.aic.crowdsourcing.persistence.dao.ArticleDAO;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Component
public class ArticleFetcherImpl implements ArticleFetcher {

    private Set<String> urls = new HashSet<String>();

    @Autowired
    private ArticleDAO articleDAO;

    private static final Logger logger = Logger
            .getLogger(ArticleFetcherImpl.class);

    public static void main(String[] args) {

    }

    @Override
    public void addFeed(String url) {
        urls.add(url);
    }

    @Override
    public Map<String, String> getNewArticles() {
        assert articleDAO != null;

        Map<String, String> ret = new HashMap<String, String>();
        for (String url : urls) {
            try {
                URL feedSource = new URL(url);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedSource));
                @SuppressWarnings("unchecked")
                List<SyndEntry> entries = feed.getEntries();
                for (SyndEntry entry : entries) {
                    if (articleDAO.findByUrl(entry.getUri()) == null) {
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
