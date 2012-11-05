package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;
import tuwien.aic.crowdsourcing.service.ArticleService;
import tuwien.aic.crowdsourcing.service.CompanyRatingService;
import tuwien.aic.crowdsourcing.service.ProductRatingService;
import tuwien.aic.crowdsourcing.service.ProductService;

@Component
public class DBTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private CompanyRatingManager companyRatingManager;

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyRatingService companyRatingService;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductRatingService productRatingService;

    public DBTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {

    }

    @Before
    public void setUp() {

    }

    @After
    @Transactional
    public void tearDown() {
        entityManager.createQuery("DELETE FROM CompanyRating").executeUpdate();
        entityManager.createQuery("DELETE FROM ProductRating").executeUpdate();

        entityManager.createQuery("DELETE FROM CompanyRecognition")
                .executeUpdate();
        entityManager.createQuery("DELETE FROM ProductRecognition")
                .executeUpdate();

        entityManager.createQuery("DELETE FROM Task").executeUpdate();
        entityManager.createQuery("DELETE FROM Article").executeUpdate();
        entityManager.createQuery("DELETE FROM Company").executeUpdate();
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }

    // @Test
    @Transactional
    public void testManagerMethods() {
        Article article1 = articleService.createArticle("Test1",
                "http://test.test");
        Article article2 = articleManager.findOne(article1.getId());
        Article article3 = articleManager.findByAddress(article1.getAddress());

        Assert.assertNotNull(article1);
        Assert.assertNotNull(article2);
        Assert.assertNotNull(article3);

        Assert.assertEquals(article1.getId(), article2.getId());
        Assert.assertEquals(article1.getTitle(), article2.getTitle());
        Assert.assertEquals(article1.getAddress(), article2.getAddress());

        Assert.assertEquals(article1.getId(), article3.getId());
        Assert.assertEquals(article1.getTitle(), article3.getTitle());
        Assert.assertEquals(article1.getAddress(), article3.getAddress());

        MWTask task1 = articleService.addTask(article3, "taskId3XYZ", "Test");

        Assert.assertNotNull(task1);
        Assert.assertNotNull(task1.getArticle());

        Assert.assertEquals(article3.getId(), task1.getArticle().getId());

        Assert.assertEquals(article3.getTitle(), task1.getArticle().getTitle());

        Assert.assertEquals(article3.getAddress(), task1.getArticle()
                .getAddress());

        Assert.assertEquals(article3.getId(), task1.getArticle().getId());

        List<MWTask> tasks = taskManager.findByTaskState(TaskState.ACTIVE);

        boolean found = false;

        for (MWTask task : tasks) {
            if ((task.getId() == task1.getId())
                    && task.getType().equals(task1.getType())
                    && task.getTaskId().equals(task1.getTaskId())
                    && (task.getTaskState() == task1.getTaskState())) {
                found = true;
            }
        }

        Assert.assertTrue(found);

        found = false;

        task1.setTaskState(TaskState.FINISHED);

        for (MWTask task : tasks) {
            if ((task.getId() == task1.getId())
                    && task.getType().equals(task1.getType())
                    && task.getTaskId().equals(task1.getTaskId())
                    && (task.getTaskState() == task1.getTaskState())) {
                found = true;
            }
        }

        Assert.assertFalse(found);

        found = false;

        task1.setTaskState(TaskState.ACTIVE);

        for (MWTask task : tasks) {
            if ((task.getId() == task1.getId())
                    && task.getType().equals(task1.getType())
                    && task.getTaskId().equals(task1.getTaskId())
                    && (task.getTaskState() == task1.getTaskState())) {
                found = true;
            }
        }

        Assert.assertTrue(found);

        Company company = new Company("TestCompany");
        company = companyManager.save(company);
        Product product = productService.addProduct("TestProduct",
                "TestCompany");

        Assert.assertEquals(0.0,
                companyRatingService.getCompanySentiment("TestCompany"), 0.001);

        Assert.assertEquals(0.0, productRatingService.getProductSentiment(
                "TestCompany", "TestProduct"), 0.001);

        companyRatingService.addCompanySentiment(task1.getTaskId(),
                "TestWorker1XYZ", "TestCompany", 5);

        productRatingService.addProductSentiment(task1.getTaskId(),
                "TestWorker1XYZ", "TestCompany", "TestProduct", -5);

        Assert.assertEquals(5.0,
                companyRatingService.getCompanySentiment("TestCompany"), 0.001);

        Assert.assertEquals(-5.0, productRatingService.getProductSentiment(
                "TestCompany", "TestProduct"), 0.001);

        Assert.assertEquals(1, productService.getProductNames("TestCompany")
                .size());

        List<String> products = productService.getProductNames("TestCompany");

        Assert.assertEquals(1, products.size());

        Assert.assertEquals("TestProduct", products.get(0));

        productService.addProduct("TestCompany", "TestProduct");

        List<String> products2 = productService.getProductNames("TestCompany");

        Assert.assertEquals(1, products2.size());

        Assert.assertEquals("TestProduct", products2.get(0));
    }
}
