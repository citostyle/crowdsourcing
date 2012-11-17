package tuwien.aic.crowdsourcing.rss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
import tuwien.aic.crowdsourcing.service.ArticleService;
import tuwien.aic.crowdsourcing.service.CompanyService;
import tuwien.aic.crowdsourcing.service.ProductService;
import tuwien.aic.crowdsourcing.service.SetupService;

@Component
public class PeriodicArticleService {

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

    private List<String> getDefaultChoices() {
        return Arrays.asList("-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3",
                "4", "5");
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
        mobileWorks.setEnvironment(new SandboxEnvironment());
        mobileWorks.setCredentials("aic12", "aic12aic");
    }

    private boolean firstRun = true;
    private Random rand = new Random();

    private List<String> getProductNames(List<Product> products) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Product p : products) {
            ret.add(productService.getNamePlusSynonyms(p));
        }
        return ret;
    }

    private List<String> getCompanyNames(List<Company> companies) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Company c : companies) {
            ret.add(companyService.getNamePlusSynonyms(c));
        }
        return ret;
    }

    private void createProductsTask(Article article) {

        try {
            List<String> names =
                    getProductNames(articleParser.getProductsInArticle(article
                            .getAddress()));
            if (names.isEmpty()) {
                return;
            }
            MWTask task = new MWTask();
            task.setTaskId("MWTask" + rand.nextInt());
            task.setType("RATE_PRODUCTS");
            task.setArticle(article);
            task.setTaskState(TaskState.ACTIVE);
            task = taskManager.save(task);
            article.getTasks().add(task);
            task.setTaskId(task.getTaskId() + "," + task.getId());

            mobileWorks.postTask(task, PRODUCT_INSTRUCTIONS, names,
                    getDefaultChoices(), WorkflowType.PARALLEL);
        } catch (IOException e) {
            System.out.println("Could not parse article "
                    + article.getAddress() + ":\n");
            e.printStackTrace();
        }
    }

    private void createCompaniesTask(Article article) {
        try {
            List<String> names =
                    getCompanyNames(articleParser.getCompaniesInArticle(article
                            .getAddress()));
            if (names.isEmpty()) {
                return;
            }

            MWTask task = new MWTask();
            task.setTaskId("MWTask" + rand.nextInt());
            task.setType("RATE_COMPANIES");
            task.setArticle(article);
            task.setTaskState(TaskState.ACTIVE);
            task = taskManager.save(task);
            article.getTasks().add(task);
            task.setTaskId(task.getTaskId() + "," + task.getId());

            mobileWorks.postTask(task, COMPANY_INSTRUCTIONS, names,
                    getDefaultChoices(), WorkflowType.PARALLEL);
        } catch (IOException e) {
            System.out.println("Could not parse article "
                    + article.getAddress() + ":\n");
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void fetchArticles() {
        if (firstRun) {
            setupService.setupTestObjects();

            firstRun = false;
        }
        Map<String, String> newArticles = articleFetcher.getNewArticles();
        System.out.println("New articles: " + newArticles);
        for (Entry<String, String> art : newArticles.entrySet()) {
            Article article =
                    articleService.createArticle(art.getValue(), art.getKey());
            createProductsTask(article);
            createCompaniesTask(article);
        }
    }
}