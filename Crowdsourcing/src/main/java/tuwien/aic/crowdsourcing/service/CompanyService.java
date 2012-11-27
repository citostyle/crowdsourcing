
package tuwien.aic.crowdsourcing.service;

import java.util.Iterator;

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
}
