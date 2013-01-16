
package tuwien.aic.crowdsourcing.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.CompanyRatingManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;
import tuwien.aic.crowdsourcing.rss.PeriodicArticleService;

@Service
public class SetupService {
    
    private static final int MAX_DAYS = 15;
    private static final int MAX_RATINGS = 2;
    
    @Autowired
    private ArticleService articleService;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private CompanyManager companyManager;
    
    @Autowired
    private CompanyRatingManager companyRatingManager;

    @Autowired
    private ProductManager productManager;
    
    @Autowired
    private ProductRatingManager productRatingManager;
    
    @Autowired
    private ProductService productService;

    private Random rand = new Random();

    private Company createCompanyIfNotExists(String name) {
        Company company1 = companyManager.findByName(name);

        if (company1 == null) {
            System.out.println(name + " does not exist, creating ...");
            company1 = new Company(name);
            company1 = companyManager.save(company1);
        }
        return company1;
    }

    @Transactional
    public void setupTestObjects() {
        Company microsoft = createCompanyIfNotExists("Microsoft");
        addCompanyRatings(microsoft);
        Company company3 = createCompanyIfNotExists("BullMarket.com");
        HashSet<String> syns3 = new HashSet<String>();
        syns3.add("bull-market.com");
        company3.setSynonyms(syns3);
        addCompanyRatings(company3);
        createCompanyIfNotExists("Pandora");
        createCompanyIfNotExists("Nuance");
        createCompanyIfNotExists("salesforce.com");
        Company mcgrawhill = createCompanyIfNotExists("McGraw-Hill");
        addCompanyRatings(mcgrawhill);
        createCompanyIfNotExists("Apollo");
        Company fusion = createCompanyIfNotExists("Fusion Analytics");
        addCompanyRatings(fusion);
        Company corning = createCompanyIfNotExists("Corning");

        Product product1 =
                productManager.findByCompanyAndName(microsoft,
                        "Microsoft Office");
        if (product1 == null) {
            product1 =
                    productService.addProduct("Microsoft Office", microsoft);
        }
        addProductRatings(product1);

        Product gorilla =
                productManager.findByCompanyAndName(corning,
                        "Gorilla glass");
        if (gorilla == null) {
            gorilla =
                    productService.addProduct("Gorilla glass", corning);
        }
        addProductRatings(gorilla);
    }
    
    private void addCompanyRatings(Company company) {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < MAX_DAYS; i++) {
            c.add(Calendar.DATE, -1);
            for (int j = 0; j < MAX_RATINGS; j++) {
                int ratingValue = rand.nextInt(10) - 4;
                
                Article article = articleService.createArticle("example_company_" + i + "_" + j, "http://example.com/company/" + i + "/" + j);
                
                MWTask task = new MWTask(article, "MWTask" + rand.nextInt(), PeriodicArticleService.RATE_COMPANIES, TaskState.PROCESSED);
                task = taskManager.save(task);
                article.getTasks().add(task);
                task.setTaskId(task.getTaskId() + "," + task.getId());
                
                CompanyRating rating = new CompanyRating(task, company, c.getTime(), ratingValue);
                rating = companyRatingManager.save(rating);
            }
        }
    }
    
    private void addProductRatings(Product product) {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < MAX_DAYS; i++) {
            c.add(Calendar.DATE, -1);
            for (int j = 0; j < MAX_RATINGS; j++) {
                int ratingValue = rand.nextInt(10) - 4;
                
                Article article = articleService.createArticle("example_product_" + i + "_" + j, "http://example.com/product/" + i + "/" + j);
                
                MWTask task = new MWTask(article, "MWTask" + rand.nextInt(), PeriodicArticleService.RATE_PRODUCTS, TaskState.PROCESSED);
                task = taskManager.save(task);
                article.getTasks().add(task);
                task.setTaskId(task.getTaskId() + "," + task.getId());
                
                ProductRating rating = new ProductRating(task, product, c.getTime(), ratingValue);
                rating = productRatingManager.save(rating);
            }
        }
    }
}
