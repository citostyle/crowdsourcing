
package tuwien.aic.crowdsourcing.rss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.mobileworks.MobileWorks;
import tuwien.aic.crowdsourcing.mobileworks.environment.SandboxEnvironment;
import tuwien.aic.crowdsourcing.mobileworks.task.WorkflowType;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;
import tuwien.aic.crowdsourcing.service.ArticleService;
import tuwien.aic.crowdsourcing.service.CompanyService;
import tuwien.aic.crowdsourcing.service.ProductService;
import tuwien.aic.crowdsourcing.service.SetupService;
import tuwien.aic.crowdsourcing.service.WorkerService;

@Component
public class PeriodicArticleService {

    public static final String RATE_PRODUCTS = "RATE_PRODUCTS";
    public static final String RATE_COMPANIES = "RATE_COMPANIES";

    @Autowired
    private ArticleFetcher articleFetcher;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MobileWorks mobileWorks;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private ArticleParser articleParser;

    @Autowired
    private SetupService setupService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private WorkerService workerService;

    private List<String> getDefaultChoices() {
        return Arrays.asList("-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3",
                "4", "5");
    }
    
    private int getDefaultRedundancy() {
        return 5;
    }

    public static final String PRODUCT_INSTRUCTIONS =
            "Please read the article with"
                    + " the given URL and then rate which "
                    + "impression of the given product(s) "
                    + "you got by reading the article. -5 is the worst rating, 5 ist the best."
                    + " Please choose NOT FOUND if the product "
                    + "does not occur in the article.";

    public static final String COMPANY_INSTRUCTIONS =
            "Please read the article with"
                    + " the given URL and then rate which "
                    + "impression of the given companies "
                    + "you got by reading the article. -5 is the worst rating, 5 ist the best."
                    + " Please choose NOT FOUND if the company "
                    + "does not occur in the article.";

    @PostConstruct
    public void postConstruct() {
        articleFetcher.addFeed("http://finance.yahoo.com/rss/usmarkets");
        articleFetcher.addFeed("http://feeds.finance.yahoo.com/rss/"
                + "2.0/category-economy-govt-and-policy?region=US&lang=en-US");
        articleFetcher.addFeed("http://localhost:8080/crowdsourcing/resources/yahoofeed.rss");
        articleFetcher.addFeed("http://finance.yahoo.com/rss/ApplicationSoftware");
        articleFetcher.addFeed("http://finance.yahoo.com/rss/PersonalComputers");
        mobileWorks.setEnvironment(new SandboxEnvironment());
        mobileWorks.setCredentials("aic12", "aic12aic");
    }

    private boolean firstRun = true;
    private Random rand = new Random();

    @Transactional
    private List<String> getProductNames(List<Product> products) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Product p : products) {
            ret.add(productService.getNamePlusSynonyms(p));
        }
        return ret;
    }

    @Transactional
    private List<String> getCompanyNames(List<Company> companies) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Company c : companies) {
            ret.add(companyService.getNamePlusSynonyms(c));
        }
        return ret;
    }

    @Transactional
    private void createProductsTask(Article article, Document doc) {
        List<String> names =
                getProductNames(articleParser.getProductsInArticle(doc));
        if (names.isEmpty()) {
            return;
        }
        System.out.println("Posting " + names + " for " + article.getAddress());
        MWTask task = new MWTask();
        task.setTaskId("MWTask" + rand.nextInt());
        task.setType(RATE_PRODUCTS);
        task.setArticle(article);
        task.setTaskState(TaskState.ACTIVE);
        task = taskManager.save(task);
        article.getTasks().add(task);
        task.setTaskId(task.getTaskId() + "," + task.getId());
        System.out.println("Posting Task " + task.getTaskId()
                + " with products " + names);
        
        int redundancy = getDefaultRedundancy();
        
        float payment = 0F;
        for (String name : names) {
            payment += productService.getPayment(name, redundancy);
        }
        payment /= names.size();
        
        List<Worker> blocked = workerService.getBadWorkers();

//        boolean res =
//                mobileWorks.postTask(task, PRODUCT_INSTRUCTIONS, names,
//                        getDefaultChoices(), WorkflowType.PARALLEL);
        boolean res =
                mobileWorks.postTask(task, PRODUCT_INSTRUCTIONS, names, 
                        getDefaultChoices(), WorkflowType.PARALLEL, 
                        redundancy, payment, blocked, null, null);
        System.out.println("MobileWorks result: " + res);
    }

    @Transactional
    private void createCompaniesTask(Article article, Document doc) {
        List<String> names =
                getCompanyNames(articleParser.getCompaniesInArticle(doc));
        if (names.isEmpty()) {
            return;
        }

        System.out.println("Posting " + names + " for " + article.getAddress());
        MWTask task = new MWTask();
        task.setTaskId("MWTask" + rand.nextInt());
        task.setType(RATE_COMPANIES);
        task.setArticle(article);
        task.setTaskState(TaskState.ACTIVE);
        task = taskManager.save(task);
        article.getTasks().add(task);
        task.setTaskId(task.getTaskId() + "," + task.getId());

        int redundancy = getDefaultRedundancy();
        
        float payment = 0F;
        for (String name : names) {
            payment += companyService.getPayment(name, redundancy);
        }
        payment /= names.size();
        
        List<Worker> blocked = workerService.getBadWorkers();

//        boolean res =
//                mobileWorks.postTask(task, COMPANY_INSTRUCTIONS, names,
//                        getDefaultChoices(), WorkflowType.PARALLEL);
        boolean res =
                mobileWorks.postTask(task, COMPANY_INSTRUCTIONS, names, 
                        getDefaultChoices(), WorkflowType.PARALLEL, 
                        redundancy, payment, blocked, null, null);
        System.out.println("MobileWorks result: " + res);
    }

    @Async
    private void parseArticle(Article article) {
        try {
            System.out.println("Parsing article " + article.getAddress());
            Document doc = articleParser.readArticle(article.getAddress());
            createProductsTask(article, doc);
            createCompaniesTask(article, doc);
        } catch (IOException e) {
            System.out.println("Could not parse article "
                    + article.getAddress() + ": " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void fetchArticles() {
        if (firstRun) {
            setupService.setupTestObjects();
            firstRun = false;
            return; // on first run only add setup objects
        }

        final Map<String, String> newArticles = articleFetcher.getNewArticles();
        for (Entry<String, String> art : newArticles.entrySet()) {
            final Article article =
                    articleService.createArticle(art.getValue(), art.getKey());
            parseArticle(article);
        }
    }
}
