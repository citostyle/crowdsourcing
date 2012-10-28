package tuwien.aic.crowdsourcing.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Company;

public interface CompanyDAO extends JpaRepository<Company, Long> {

    Company findById(int id);
    
    Company findByName(String name);

}
