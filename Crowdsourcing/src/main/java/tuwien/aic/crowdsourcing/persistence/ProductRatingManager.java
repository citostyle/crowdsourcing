package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface ProductRatingManager extends
        JpaRepository<ProductRating, Long> {

    ProductRating findByTaskAndWorkerAndProduct(MWTask task, Worker worker,
            Product product);

    List<ProductRating> findByProduct(Product p);

}
