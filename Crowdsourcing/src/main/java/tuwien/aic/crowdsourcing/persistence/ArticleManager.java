package tuwien.aic.crowdsourcing.persistence;

import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;

public interface ArticleManager {

    public Article getArticleById(long id);

    public Article getArticleByAddress(String address);
    
    public Article createArticle(String title, 
                                 String address);

    public MWTask addTask(Article article, 
                          String taskId, 
                          String type);
}
