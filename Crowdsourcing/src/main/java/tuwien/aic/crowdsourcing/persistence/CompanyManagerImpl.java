package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tuwien.aic.crowdsourcing.persistence.entities.Company;

public class CompanyManagerImpl implements CompanyManager {

    @PersistenceContext
    private EntityManager entityManager = null;

    @Override
    public void addCompanySynonym(String companyName, String synonym) {
        CompanyManager companyManager =  new CompanyManagerImpl();
        
        Company company = 
            companyManager.getCompanyByName(companyName);
        
        if (company == null) {
            throw new IllegalArgumentException
                ("The requested company does not exist!");
        }
        
        company.getSynonyms().add(synonym);
        
        entityManager.merge(company);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Company getCompanyByName(String companyName) {
        Company ret = null;

        List<Company> companies = entityManager
                .createQuery(
                        "SELECT c FROM Company c WHERE c.name = :companyName")
                .setParameter("companyName", companyName)
                .getResultList();

        if (!companies.isEmpty()) {
            ret = companies.get(0);
        }

        return ret;
    }
    
}
