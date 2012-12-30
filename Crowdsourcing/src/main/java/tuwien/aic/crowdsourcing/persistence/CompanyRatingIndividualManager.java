
package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRatingIndividual;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface CompanyRatingIndividualManager extends
        JpaRepository<CompanyRatingIndividual, Long> {

    List<CompanyRatingIndividual> findByWorker(Worker worker);

    @Query("SELECT avg(timeTaken) FROM CompanyRatingIndividual r WHERE r.rating = ?1")
    Long getAvgTimeTakenByRating(CompanyRating rating);

    @Query("SELECT w FROM CompanyRatingIndividual r JOIN r.worker w WHERE r.bad = true GROUP BY w HAVING count(w) > 5")
    List<Worker> findBadWorkers();

}
