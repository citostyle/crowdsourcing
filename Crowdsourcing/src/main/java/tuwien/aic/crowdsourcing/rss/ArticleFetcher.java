package tuwien.aic.crowdsourcing.rss;

import java.util.Map;

public interface ArticleFetcher {

    void addFeed(String url);

    Map<String, String> getNewArticles();

}
