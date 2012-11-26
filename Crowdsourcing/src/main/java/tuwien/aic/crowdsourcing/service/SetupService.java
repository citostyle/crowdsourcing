
package tuwien.aic.crowdsourcing.service;

import java.util.HashSet;

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

    private Company createCompanyIfNotExists(String name) {
        Company company1 = companyManager.findByName(name);

        if (company1 == null) {
            System.out.println(name + " does not exist, creating ...");
            company1 = new Company(name);
            company1 = companyManager.save(company1);
        }
        return company1;
    }

    @Transactional
    public void setupTestObjects() {
        Company company1 = createCompanyIfNotExists("Microsoft");
        Company company2 = createCompanyIfNotExists("Apple Corp.");
        Company company3 = createCompanyIfNotExists("BullMarket.com");
        HashSet<String> syns = new HashSet<String>();
        syns.add("Teh GR8est");
        company3.setSynonyms(syns);
        createCompanyIfNotExists("Fibonacci");
        createCompanyIfNotExists("Pandora");
        createCompanyIfNotExists("Nuance");
        createCompanyIfNotExists("salesforce.com");

        Product product1 =
                productManager.findByCompanyAndName(company1,
                        "Microsoft Office");
        Product product2 =
                productManager.findByCompanyAndName(company2, "Macintosh");
        if (product1 == null) {
            product1 =
                    productService.addProduct("Microsoft Office", "Microsoft");
        }
        if (product2 == null) {
            product1 = productService.addProduct("Macintosh", "Apple Corp.");
        }
        company1.getProducts().add(product1);
        company2.getProducts().add(product2);
        company1 = companyManager.save(company1);
        company2 = companyManager.save(company2);

    }
}
