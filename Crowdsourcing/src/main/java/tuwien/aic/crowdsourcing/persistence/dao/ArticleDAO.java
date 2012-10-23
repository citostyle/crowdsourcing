package tuwien.aic.crowdsourcing.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Article;

public interface ArticleDAO extends JpaRepository<Article, Long> {

    boolean exists(String url);

}
