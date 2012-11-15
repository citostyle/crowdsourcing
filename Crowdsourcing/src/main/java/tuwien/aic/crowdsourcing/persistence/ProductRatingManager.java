package tuwien.aic.crowdsourcing.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface ProductRatingManager extends
        JpaRepository<ProductRating, Long> {

    ProductRating findByTaskAndWorkerAndProduct(MWTask task, Worker worker,
            Product product);

    List<ProductRating> findByProduct(Product product);
    
    @Query("SELECT r FROM ProductRating r WHERE r.product = ?1 AND " +
                "lastModified >= ?2")
    List<ProductRating> findByProduct(Product product, Date start);

    @Query("SELECT r FROM ProductRating r WHERE r.product = ?1 AND " +
                "lastModified BETWEEN ?2 AND ?3")
    List<ProductRating> findByProduct(Product product, Date start, Date limit);

}
