
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
        HashSet<String> syns1 = new HashSet<String>();
        syns1.add("bull-market.com");
        company1.setSynonyms(syns1);
        Company company3 = createCompanyIfNotExists("BullMarket.com");
        HashSet<String> syns3 = new HashSet<String>();
        syns3.add("bull-market.com");
        company3.setSynonyms(syns3);
        createCompanyIfNotExists("Pandora");
        createCompanyIfNotExists("Nuance");
        createCompanyIfNotExists("salesforce.com");
        createCompanyIfNotExists("McGraw-Hill");
        createCompanyIfNotExists("Apollo");
        createCompanyIfNotExists("Fusion Analytics");

        Product product1 =
                productManager.findByCompanyAndName(company1,
                        "Microsoft Office");
        if (product1 == null) {
            product1 =
                    productService.addProduct("Microsoft Office", "Microsoft");
        }
        company1.getProducts().add(product1);
        company1 = companyManager.save(company1);

    }
}
