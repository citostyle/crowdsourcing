package tuwien.aic.crowdsourcing.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Article;

public interface ArticleDAO extends JpaRepository<Article, Long> {

    Article findById(int id);
    
    Article findByUrl(String url);
    
    Article findByTitle(String title);

}
