package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

public class ArticleManagerImpl implements ArticleManager {
    @PersistenceContext
    private EntityManager entityManager = null;

    public ArticleManagerImpl() {
        
    }
    
    @Override
    public Article getArticleById(long id) {
        return entityManager.find(Article.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Article getArticleByGuid(String guid) {
        Article ret = null;
        
        List<Article> articles =
            (List<Article>)entityManager.createQuery
                ("SELECT a FROM Article a WHERE a.guid = :guid")
                    .setParameter("guid", guid).getResultList();
        
        if (!articles.isEmpty()) {
            ret = articles.get(0);
        }
        
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Article getArticleByAddress(String address) {
        Article ret = null;
        
        List<Article> articles =
            (List<Article>)entityManager.createQuery
                ("SELECT a FROM Article a WHERE a.address = :address")
                    .setParameter("address", address).getResultList();
        
        if (!articles.isEmpty()) {
            ret = articles.get(0);
        }
        
        return ret;
    }

    @Override
    public Article createArticle(String guid, String title, String address) {
        Article article = getArticleByGuid(guid);
        
        if (article == null) {
            article = new Article(guid, title, address);
            
            entityManager.persist(article);
            
            entityManager.refresh(article);
        }
        
        return article;
    }

    @Override
    public MWTask addTask(Article article, String taskId, String type) {
        
        if (article == null) {
            throw new IllegalArgumentException
                ("The argument 'article' is not allowed to be NULL!");
        }
        
        MWTask ret = new MWTask(article, taskId, type, TaskState.ACTIVE);
        
        entityManager.persist(ret);
            
        entityManager.refresh(ret);
        
        entityManager.refresh(article);
        
        return ret;
    }
}
