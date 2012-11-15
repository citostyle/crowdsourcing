package tuwien.aic.crowdsourcing.rss;

import java.util.*;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.ArticleManager;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.service.ArticleService;
import tuwien.aic.crowdsourcing.service.CompanyRatingService;
import tuwien.aic.crowdsourcing.service.ProductRatingService;
import tuwien.aic.crowdsourcing.service.SetupService;

@Component
public class PeriodicArticleService {

    @Autowired
    private ArticleFetcher articleFetcher;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private ProductRatingService productRatingService;
    
    @Autowired
    private CompanyRatingService companyRatingService;

    @Autowired
    private SetupService setupService;

    @PostConstruct
    public void postConstruct() {
        articleFetcher.addFeed("http://finance.yahoo.com/rss/usmarkets",
                Arrays.asList("yahoo"));
    }

    private int counter = 0;
    private boolean firstRun = true;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void fetchArticles() {
        if (firstRun) {
            setupService.setupTestObjects();

            firstRun = false;
        }
        Map<String, String> newArticles = articleFetcher.getNewArticles();
        for (Entry<String, String> art : newArticles.entrySet()) {
            Article art2 =
                    articleService.createArticle(art.getValue(), art.getKey());
            counter = new Random().nextInt();
            articleService.addTask(art2, "id" + counter, "questions");
            companyRatingService.addCompanySentiment("id" + counter, "worker1",
                    "MSFT", 3);
            
            productRatingService.addProductSentiment("id" + counter, "worker1", 
                                                     "MSFT", "XYZ", 1);
        }
        Map<String, String> oldArticles = articleFetcher.getOldArticles();
        for (Entry<String, String> art : oldArticles.entrySet()) {
            Article art2 =
                    articleService.createArticle(art.getValue(), art.getKey());
            counter = new Random().nextInt();
            articleService.addTask(art2, "id" + counter, "questions");
            companyRatingService.addCompanySentiment("id" + counter, "worker1",
                    "MSFT", new Random().nextInt(5));
            
            productRatingService.addProductSentiment("id" + counter, "worker1", 
                                                     "MSFT", "XYZ", 1);
        }
        
        System.out.println(articleManager.findAll());
        
        Calendar calendar = 
            Calendar.getInstance();
        
        calendar.setTime(new Date());
        
        calendar.add(Calendar.DATE, -1);
        
        System.out.println(companyRatingService.getCompanySentiment("MSFT"));
        
        System.out.println(companyRatingService.getCompanySentiment("MSFT", 
                                                                    calendar.getTime()));
        
        System.out.println(companyRatingService.getCompanySentiment("MSFT", 
                                                                    calendar.getTime(), new Date()));
        
        System.out.println(productRatingService.getProductSentiment("MSFT", "Office"));
        
        System.out.println(productRatingService.getProductSentiment("MSFT", "Office",
                                                                    calendar.getTime()));
        
        System.out.println(productRatingService.getProductSentiment("MSFT", "Office",
                                                                    calendar.getTime(), new Date()));
        
    }
}