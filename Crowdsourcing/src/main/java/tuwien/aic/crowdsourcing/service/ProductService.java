package tuwien.aic.crowdsourcing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Service
public class ProductService {

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private ProductManager productManager;

    public void addProductSynonym(String companyName, String productName,
            String synonym) {
        Company company = companyManager.findByName(companyName);
        if (company == null) {
            throw new IllegalArgumentException(
                    "The requested company does not exist!");
        }

        Product product = productManager.findByCompanyAndName(company,
                productName);
        if (product == null) {
            throw new IllegalArgumentException(
                    "The requested product does not exist!");
        }

        product.getSynonyms().add(synonym);
        productManager.save(product);
    }

    public List<String> getProductNames(String companyName) {
        Company company = companyManager.findByName(companyName);
        if (company == null) {
            throw new IllegalArgumentException("company not found: "
                    + companyName);
        }
        List<Product> products = productManager.findByCompany(company);
        List<String> ret = new ArrayList<String>();

        for (Product p : products) {
            ret.add(p.getName());
        }
        return ret;
    }

}
