package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Repository
@Transactional
public class ProductManagerImpl implements ProductManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product addProduct(String companyName, String productName) {
        CompanyManager companyManager =  new CompanyManagerImpl();
        
        Company company = 
            companyManager.getCompanyByName(companyName);
        
        if (company == null) {
            company = new Company(companyName);

            entityManager.persist(company);

            entityManager.refresh(company);
        }
        
        Product product = getProductByName(companyName,
                                           productName);
        
        if (product == null) {
            product = new Product(company, productName);
        
            entityManager.persist(product);

            entityManager.refresh(product);
        }
        
        return product;
    }

    @Override
    public void addProductSynonym(String companyName,
                                  String productName, 
                                  String synonym) {
        
        CompanyManager companyManager =  new CompanyManagerImpl();
        
        Company company = 
            companyManager.getCompanyByName(companyName);
        
        if (company == null) {
            throw new IllegalArgumentException
                ("The requested company does not exist!");
        }
        
        Product product = getProductByName(companyName,
                                           productName);
        
        if (product == null) {
            throw new IllegalArgumentException
                ("The requested product does not exist!");
        }
        
        product.getSynonyms().add(synonym);
        
        entityManager.merge(product);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Product getProductByName(String companyName,
                                    String productName) {
        Product ret = null;

        List<Product> products = entityManager
                .createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE p.name = :productName AND" +
                        "      p.company.name = :companyName")
                .setParameter("companyName", productName)
                .setParameter("productName", productName)
                .getResultList();

        if (!products.isEmpty()) {
            ret = products.get(0);
        }

        return ret;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getProductNames(String companyName) {

        List<String> ret = entityManager
                .createQuery(
                        "SELECT DISTINCT p.name FROM Product p " +
                        "WHERE p.company.name = :companyName " +
                        "ORDER BY p.name")
                .setParameter("companyName", companyName)
                .getResultList();

        return ret;
    }    
}
