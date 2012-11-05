package tuwien.aic.crowdsourcing.persistence;

import tuwien.aic.crowdsourcing.persistence.entities.Company;

public interface CompanyManager {
    
    void addCompanySynonym(String companyName,
                           String synonym);
    
    Company getCompanyByName(String companyName);
}
