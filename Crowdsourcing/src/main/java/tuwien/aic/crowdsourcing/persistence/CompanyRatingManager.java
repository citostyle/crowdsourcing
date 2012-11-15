package tuwien.aic.crowdsourcing.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface CompanyRatingManager extends
        JpaRepository<CompanyRating, Long> {

    List<CompanyRating> findByCompany(Company company);
    
    @Query("SELECT r FROM CompanyRating r WHERE r.company = ?1 AND " +
                "lastModified >= ?2")
    List<CompanyRating> findByCompany(Company company, Date start);

    @Query("SELECT r FROM CompanyRating r WHERE r.company = ?1 AND " +
                "lastModified BETWEEN ?2 AND ?3")
    List<CompanyRating> findByCompany(Company company, Date start, Date limit);

    CompanyRating findByTaskAndWorkerAndCompany(MWTask task, Worker worker,
            Company company);
    
}
