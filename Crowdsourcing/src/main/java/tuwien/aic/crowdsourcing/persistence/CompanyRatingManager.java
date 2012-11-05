package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface CompanyRatingManager extends
        JpaRepository<CompanyRating, Long> {

    List<CompanyRating> findByCompany(Company company);

    CompanyRating findByTaskAndWorkerAndCompany(MWTask task, Worker worker,
            Company company);

}
