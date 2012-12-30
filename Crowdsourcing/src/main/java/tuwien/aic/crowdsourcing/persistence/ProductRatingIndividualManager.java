
package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRatingIndividual;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface ProductRatingIndividualManager extends
        JpaRepository<ProductRatingIndividual, Long> {

    List<ProductRatingIndividual> findByWorker(Worker worker);

    @Query("SELECT avg(timeTaken) FROM ProductRatingIndividual r WHERE r.rating = ?1")
    Long getAvgTimeTakenByRating(ProductRating rating);

    @Query("SELECT w FROM ProductRatingIndividual r JOIN r.worker w WHERE r.bad = true GROUP BY w HAVING count(w) > 5")
    List<Worker> findBadWorkers();

}
