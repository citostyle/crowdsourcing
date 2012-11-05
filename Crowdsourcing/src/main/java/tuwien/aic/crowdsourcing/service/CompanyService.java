package tuwien.aic.crowdsourcing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;

@Service
public class CompanyService {

    @Autowired
    private CompanyManager companyManager;

    @Transactional
    public void addCompanySynonym(String companyName, String synonym) {
        Company company = companyManager.findByName(companyName);

        if (company == null) {
            throw new IllegalArgumentException(
                    "The requested company does not exist!");
        }

        company.getSynonyms().add(synonym);
        companyManager.save(company);
    }
}
