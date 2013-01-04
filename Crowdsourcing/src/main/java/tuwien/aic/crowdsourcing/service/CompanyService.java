
package tuwien.aic.crowdsourcing.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.CompanyRatingManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;

@Service
public class CompanyService {

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private CompanyRatingManager companyRatingManager;

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

    public String getNamePlusSynonyms(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(company.getName());
        if (!company.getSynonyms().isEmpty()) {
            sb.append(" (also kown as: ");
            Iterator<String> it = company.getSynonyms().iterator();
            while (it.hasNext()) {
                sb.append(it.next());
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
    
    @Transactional
    public double getPayment(String companyName, int redundancy) {
        Company company = companyManager.findByName(companyName);

        if (company == null) {
            throw new IllegalArgumentException(
                    "The requested company does not exist!");
        }
        
        Double timeTaken = companyRatingManager.getAvgTimeTaken(company);
        if (timeTaken == null || timeTaken == 0)
            timeTaken = 120D; // default value
        
        return timeTaken * (1F / 3600F * 500F) * redundancy;
    }
}
