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
    @Autowired
    private ProductService productService;

    @Transactional
    public void setupTestObjects() {
        Company company1 = companyManager.findByName("MSFT");
        Company company2 = companyManager.findByName("AAPL");
        Company company3 = companyManager.findByName("NASDAQ");
        if (company1 == null) {
            company1 = new Company("MSFT");
            company1 = companyManager.save(company1);
        }
        if (company2 == null) {
            company2 = new Company("AAPL");
            company2 = companyManager.save(company2);
        }
        if (company3 == null) {
            company3 = new Company("NASDAQ");
            company3 = companyManager.save(company3);
        }
        Product product1 =
                productManager.findByCompanyAndName(company1,
                        "Microsoft Office");
        Product product2 =
                productManager.findByCompanyAndName(company2, "Macintosh");
        if (product1 == null) {
            product1 = productService.addProduct("MSFT", "Microsoft Office");
        }
        if (product2 == null) {
            product1 = productService.addProduct("AAPL", "Macintosh");
        }
        company1.getProducts().add(product1);
        company2.getProducts().add(product2);
        company1 = companyManager.save(company1);
        company2 = companyManager.save(company2);

    }

}
