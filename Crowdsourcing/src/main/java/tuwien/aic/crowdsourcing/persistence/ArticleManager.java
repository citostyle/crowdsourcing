package tuwien.aic.crowdsourcing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Article;

public interface ArticleManager extends JpaRepository<Article, Long> {

    public Article findByAddress(String address);
}
