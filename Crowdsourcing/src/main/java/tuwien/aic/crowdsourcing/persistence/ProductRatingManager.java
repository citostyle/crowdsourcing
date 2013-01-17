
package tuwien.aic.crowdsourcing.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;

public interface ProductRatingManager extends
        JpaRepository<ProductRating, Long> {

    List<ProductRating> findByProduct(Product product);

    List<ProductRating> findByTask(MWTask task);

    ProductRating findByTaskAndProduct(MWTask task, Product product);

    @Query("SELECT r FROM ProductRating r WHERE r.product = ?1 AND " +
            "lastModified >= ?2")
    List<ProductRating> findByProduct(Product product, Date start);

    @Query("SELECT r FROM ProductRating r WHERE r.product = ?1 AND " +
            "lastModified BETWEEN ?2 AND ?3")
    List<ProductRating> findByProduct(Product product, Date start, Date limit);

    @Query("SELECT count(*) FROM ProductRating r WHERE r.product = ?1")
    Long getNumRatings(Product product);

    @Query("SELECT count(*) FROM ProductRating r WHERE r.product = ?1 AND lastModified >= ?2")
    Long getNumRatings(Product product, Date start);

    @Query("SELECT count(*) FROM ProductRating r WHERE r.product = ?1 AND lastModified BETWEEN ?2 AND ?3")
    Long getNumRatings(Product product, Date start, Date limit);

    @Query("SELECT avg(ir.timeTaken) FROM ProductRating r JOIN r.individualRatings ir WHERE r.product = ?1")
    Double getAvgTimeTaken(Product product);

}
