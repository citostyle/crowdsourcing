package tuwien.aic.crowdsourcing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuwien.aic.crowdsourcing.persistence.ArticleManager;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

@Service
public class ArticleService {

    @Autowired
    private ArticleManager articleManager;

    @Autowired
    private TaskManager taskManager;

    public Article createArticle(String title, String address) {
        Article article = articleManager.findByAddress(address);
        if (article == null) {
            article = new Article(title, address);
            article = articleManager.save(article);
        }
        return article;
    }

    public MWTask addTask(Article article, String taskId, String type) {
        if (article == null) {
            throw new IllegalArgumentException(
                    "The argument 'article' is not allowed to be NULL!");
        }
        MWTask ret = new MWTask(article, taskId, type, TaskState.ACTIVE);
        ret = taskManager.save(ret);
        return ret;
    }

    // public void resetDatabase() {
    // entityManager.createQuery("DELETE FROM Correlation").executeUpdate();
    //
    // entityManager.createQuery("DELETE FROM CompanyRating").executeUpdate();
    // entityManager.createQuery("DELETE FROM ProductRating").executeUpdate();
    //
    // entityManager.createQuery("DELETE FROM CompanyRecognition")
    // .executeUpdate();
    // entityManager.createQuery("DELETE FROM ProductRecognition")
    // .executeUpdate();
    //
    // entityManager.createQuery("DELETE FROM Task").executeUpdate();
    // entityManager.createQuery("DELETE FROM Article").executeUpdate();
    // entityManager.createQuery("DELETE FROM Company").executeUpdate();
    // entityManager.createQuery("DELETE FROM Product").executeUpdate();
    // }
    //
    // public void testManagerMethods() {
    // TaskManager taskManager = new TaskManagerImpl();
    // ArticleManager articleManager = new ArticleManagerImpl();
    // ProductManager productManager = new ProductManagerImpl();
    // SentimentManager sentimentManager = new SentimentManagerImpl();
    //
    // Article article1 = articleManager.createArticle("Test1",
    // "http://test.test");
    // Article article2 = articleManager.getArticleById(article1.getId());
    // Article article3 = articleManager.getArticleByAddress(article1
    // .getAddress());
    //
    // if (article1 != null) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (article2 != null) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (article3 != null) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (article1.getId() != article2.getId()) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!article1.getTitle().equals(article2.getTitle())) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!article1.getAddress().equals(article2.getAddress())) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (article1.getId() != article3.getId()) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!article1.getTitle().equals(article3.getTitle())) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!article1.getAddress().equals(article3.getAddress())) {
    // System.err.println("ERROR!");
    // return;
    // }
    // MWTask task1 = articleManager.addTask(article3, "taskId3XYZ", "Test");
    // if (task1 != null) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (task1.getArticle() != null) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (article3.getId() != task1.getArticle().getId()) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!article3.getTitle().equals(task1.getArticle().getTitle())) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!article3.getAddress().equals(task1.getArticle().getAddress())) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (taskManager.getResponseCount(task1.getTaskId()) != 0) {
    // System.err.println("ERROR!");
    // return;
    // }
    // List<MWTask> tasks = taskManager.getActiveTasks();
    // boolean found = false;
    // for (MWTask task : tasks) {
    // if ((task.getId() == task1.getId())
    // && task.getType().equals(task1.getType())
    // && task.getTaskId().equals(task1.getTaskId())
    // && (task.getTaskState() == task1.getTaskState())) {
    // found = true;
    // }
    // }
    // if (!found) {
    // System.err.println("ERROR!");
    // return;
    // }
    // found = false;
    // taskManager.setTaskState(task1.getTaskId(), TaskState.FINISHED);
    // for (MWTask task : tasks) {
    // if ((task.getId() == task1.getId())
    // && task.getType().equals(task1.getType())
    // && task.getTaskId().equals(task1.getTaskId())
    // && (task.getTaskState() == task1.getTaskState())) {
    // found = true;
    // }
    // }
    // if (found) {
    // System.err.println("ERROR!");
    // return;
    // }
    // found = false;
    // taskManager.setTaskState(task1.getTaskId(), TaskState.ACTIVE);
    // for (MWTask task : tasks) {
    // if ((task.getId() == task1.getId())
    // && task.getType().equals(task1.getType())
    // && task.getTaskId().equals(task1.getTaskId())
    // && (task.getTaskState() == task1.getTaskState())) {
    // found = true;
    // }
    // }
    // if (!found) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (sentimentManager.getCompanySentiment("TestCompany") != 0.0) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (sentimentManager.getProductSentiment("TestCompany", "TestProduct") !=
    // 0.0) {
    // System.err.println("ERROR!");
    // return;
    // }
    // sentimentManager.addCompanySentiment(task1.getTaskId(),
    // "TestWorker1XYZ", "TestCompany", 5);
    // sentimentManager.addProductSentiment(task1.getTaskId(),
    // "TestWorker1XYZ", "TestCompany", "TestProduct", -5);
    // if (sentimentManager.getCompanySentiment("TestCompany") != 5.0) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (sentimentManager.getProductSentiment("TestCompany", "TestProduct") !=
    // -5.0) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (productManager.getProductNames("TestCompany").size() != 1) {
    // System.err.println("ERROR!");
    // return;
    // }
    //
    // productManager.addProduct("TestCompany", "TestProduct");
    // List<String> products = productManager.getProductNames("TestCompany");
    // if (products.size() != 1) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!products.get(0).equals("TestProduct")) {
    // System.err.println("ERROR!");
    // return;
    // }
    //
    // productManager.addProduct("TestCompany", "TestProduct");
    // List<String> products2 = productManager.getProductNames("TestCompany");
    // if (products2.size() != 1) {
    // System.err.println("ERROR!");
    // return;
    // }
    // if (!products2.get(0).equals("TestProduct")) {
    // System.err.println("ERROR!");
    // return;
    // }
    //
    // System.out.println("SUCCESS!");
    // }

}
