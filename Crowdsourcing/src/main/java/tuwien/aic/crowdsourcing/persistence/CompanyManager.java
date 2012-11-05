package tuwien.aic.crowdsourcing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Company;

public interface CompanyManager extends JpaRepository<Company, Long> {

    Company findByName(String companyName);
}
