package tuwien.aic.crowdsourcing.rss;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class ArticleFetcher {

    public Map<String, String> fetchArticles() throws IllegalArgumentException,
            FeedException, IOException {
        URL feedSource = new URL("http://finance.yahoo.com/rss/usmarkets");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        @SuppressWarnings("unchecked")
        List<SyndEntry> entries = feed.getEntries();
        Map<String, String> ret = new HashMap<String, String>();
        for (SyndEntry entry : entries) {
            ret.put(entry.getUri(), entry.getTitle());
        }
        return ret;
    }

    public static void main(String[] args) throws IllegalArgumentException,
            FeedException, IOException {
        for (Entry<String, String> entry : new ArticleFetcher().fetchArticles()
                .entrySet()) {
            System.out.println(entry.getValue() + ": " + entry.getKey());
        }
    }
}
