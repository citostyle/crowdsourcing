package tuwien.aic.crowdsourcing.rss;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tuwien.aic.crowdsourcing.persistence.ArticleManager;

@Component
public class PeriodicArticleService {

    @Autowired
    private ArticleFetcher articleFetcher;

    @Autowired
    private ArticleManager articleManager;

    @PostConstruct
    public void postConstruct() {
        articleFetcher.addFeed("http://finance.yahoo.com/rss/usmarkets");
    }

    @Scheduled(fixedRate = 5000)
    public void fetchArticles() {
        Map<String, String> newArticles = articleFetcher.getNewArticles();
        for (Entry<String, String> art : newArticles.entrySet()) {
            articleManager.createArticle(art.getValue(), art.getKey());
        }
        // TODO add new articles to DB
        System.out.println(newArticles);
    }
}