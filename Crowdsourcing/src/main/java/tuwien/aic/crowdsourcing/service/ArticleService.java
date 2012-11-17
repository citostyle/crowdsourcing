package tuwien.aic.crowdsourcing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Article createArticle(String title, String address) {
        Article article = articleManager.findByAddress(address);
        if (article == null) {
            article = new Article(title, address);
            article = articleManager.save(article);
        }
        return article;
    }

    @Transactional
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

}
