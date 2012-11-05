package tuwien.aic.crowdsourcing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Service
public class SetupService {

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private ProductManager productManager;

    @Transactional
    public void setupTestObjects() {
        Company company1 = companyManager.findByName("MSFT");
        Company company2 = companyManager.findByName("AAPL");
        if (company1 == null) {
            company1 = new Company("MSFT");
            company1 = companyManager.save(company1);
        }
        if (company2 == null) {
            company2 = new Company("AAPL");
            company2 = companyManager.save(company2);
        }
        Product product1 = productManager.findByName("Office");
        Product product2 = productManager.findByName("Mac");
        if (product1 == null) {
            product1 = new Product(company1, "Office");
            product1 = productManager.save(product1);
        }
        if (product2 == null) {
            product2 = new Product(company2, "Mac");
            product2 = productManager.save(product2);
        }
        company1.getProducts().add(product1);
        company2.getProducts().add(product2);
        company1 = companyManager.save(company1);
        company2 = companyManager.save(company2);

    }

}
