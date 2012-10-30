package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.*;
import org.springframework.transaction.annotation.Transactional;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

public class DBTest {
    
    @PersistenceContext
    private EntityManager entityManager = null;

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
        entityManager.createQuery("DELETE FROM Correlation").executeUpdate();
        
        entityManager.createQuery("DELETE FROM CompanyRating").executeUpdate();
        entityManager.createQuery("DELETE FROM ProductRating").executeUpdate();
        
        entityManager.createQuery("DELETE FROM CompanyRecognition").executeUpdate();
        entityManager.createQuery("DELETE FROM ProductRecognition").executeUpdate();
        
        entityManager.createQuery("DELETE FROM Task").executeUpdate();
        entityManager.createQuery("DELETE FROM Article").executeUpdate();
        entityManager.createQuery("DELETE FROM Company").executeUpdate();
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
    }
    
    @Test
    @Transactional
    public void testManagerMethods() {
        TaskManager taskManager = new TaskManagerImpl();
        ArticleManager articleManager = new ArticleManagerImpl();
        SentimentManager sentimentManager = new SentimentManagerImpl();
        
        Article article1 = 
            articleManager.createArticle("Test1", "http://test.test");
        
        Article article2 =
            articleManager.getArticleById(article1.getId());
        
        Article article3 = 
            articleManager.getArticleByAddress(article1.getAddress());
        
        Assert.assertNotNull(article1);
        Assert.assertNotNull(article2);
        Assert.assertNotNull(article3);
        
        Assert.assertEquals(article1.getId(), article2.getId());
        Assert.assertEquals(article1.getTitle(), article2.getTitle());
        Assert.assertEquals(article1.getAddress(), article2.getAddress());
        
        Assert.assertEquals(article1.getId(), article3.getId());
        Assert.assertEquals(article1.getTitle(), article3.getTitle());
        Assert.assertEquals(article1.getAddress(), article3.getAddress());
        
        MWTask task1 = articleManager.addTask(article3, "taskId3XYZ", "Test");
        
        Assert.assertNotNull(task1);
        Assert.assertNotNull(task1.getArticle());
        
        Assert.assertEquals(article3.getId(), 
                            task1.getArticle().getId());
        
        Assert.assertEquals(article3.getTitle(), 
                            task1.getArticle().getTitle());
        
        Assert.assertEquals(article3.getAddress(), 
                            task1.getArticle().getAddress());
        
        Assert.assertEquals(article3.getId(), task1.getArticle().getId());
        
        Assert.assertEquals(0, taskManager.getResponseCount(task1.getTaskId()));
        
        List<MWTask> tasks = taskManager.getActiveTasks();
        
        boolean found = false;
        
        for (MWTask task : tasks) {
            if (task.getId() == task1.getId() && 
                task.getType().equals(task1.getType()) &&
                task.getTaskId().equals(task1.getTaskId()) &&
                task.getTaskState() == task1.getTaskState()) {
                found = true;
            }
        }
        
        Assert.assertTrue(found);
        
        found = false;
        
        taskManager.setTaskState(task1.getTaskId(), TaskState.FINISHED);  
        
        for (MWTask task : tasks) {
            if (task.getId() == task1.getId() && 
                task.getType().equals(task1.getType()) &&
                task.getTaskId().equals(task1.getTaskId()) &&
                task.getTaskState() == task1.getTaskState()) {
                found = true;
            }
        }
        
        Assert.assertFalse(found);
        
        found = false;
        
        taskManager.setTaskState(task1.getTaskId(), TaskState.ACTIVE);  
        
        for (MWTask task : tasks) {
            if (task.getId() == task1.getId() && 
                task.getType().equals(task1.getType()) &&
                task.getTaskId().equals(task1.getTaskId()) &&
                task.getTaskState() == task1.getTaskState()) {
                found = true;
            }
        }
        
        Assert.assertTrue(found);
        
        Assert.assertEquals
            (0.0, sentimentManager.getCompanySentiment("TestCompany"), 0.001);
        
        Assert.assertEquals 
            (0.0, sentimentManager.getProductSentiment("TestProduct"), 0.001);
        
        sentimentManager.addCompanySentiment(task1.getTaskId(), 
                                             "TestWorker1XYZ", 
                                             "TestCompany", 
                                             5);
        
        sentimentManager.addProductSentiment(task1.getTaskId(), 
                                             "TestWorker1XYZ", 
                                             "TestProduct", 
                                             -5);
        
        Assert.assertEquals
            (5.0, sentimentManager.getCompanySentiment("TestCompany"), 0.001);
        
        Assert.assertEquals 
            (-5.0, sentimentManager.getProductSentiment("TestProduct"), 0.001);
        
        Assert.assertEquals(0, sentimentManager.getProductNames("TestCompany").size());
        
        sentimentManager.addCorrelation(task1.getTaskId(), 
                                        "TestWorker1XYZ", 
                                        "TestCompany",
                                        "TestProduct");
        
        List<String> products = 
            sentimentManager.getProductNames("TestCompany");
        
        Assert.assertEquals(1, products.size());
        
        Assert.assertEquals("TestProduct", products.get(0));
        
        sentimentManager.addCorrelation(task1.getTaskId(), 
                                        "TestWorker1XYZ", 
                                        "TestCompany",
                                        "TestProduct");
        
        List<String> products2 = 
            sentimentManager.getProductNames("TestCompany");
        
        Assert.assertEquals(1, products2.size());
        
        Assert.assertEquals("TestProduct", products2.get(0));
    }
}
