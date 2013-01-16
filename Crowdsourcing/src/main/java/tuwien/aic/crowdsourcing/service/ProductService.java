
package tuwien.aic.crowdsourcing.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Service
public class ProductService {

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductRatingManager productRatingManager;

    @Transactional
    public void addProductSynonym(String companyName, String productName,
            String synonym) {
        Company company = companyManager.findByName(companyName);
        if (company == null) {
            throw new IllegalArgumentException(
                    "The requested company does not exist!");
        }

        Product product =
                productManager.findByCompanyAndName(company, productName);
        if (product == null) {
            throw new IllegalArgumentException(
                    "The requested product does not exist!");
        }

        product.getSynonyms().add(synonym);
        productManager.save(product);
    }

    @Transactional
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

    @Transactional
    public Product addProduct(String productName, Company company) {
        Product p = new Product(company, productName);
        p = productManager.save(p);
        company.getProducts().add(p);
        companyManager.save(company);
        return p;
    }

    public String getNamePlusSynonyms(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getName());
        if (!product.getSynonyms().isEmpty()) {
            sb.append(" (also kown as: ");
            Iterator<String> it = product.getSynonyms().iterator();
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
    public double getPayment(String productName, int redundancy) {
        Product product = productManager.findByName(productName);

        if (product == null) {
            throw new IllegalArgumentException(
                    "The requested product does not exist!");
        }
        
        Double timeTaken = productRatingManager.getAvgTimeTaken(product);
        if (timeTaken == null || timeTaken == 0)
            timeTaken = 120D; // default value
        
        return (timeTaken * (1F / 3600F * 500F) * redundancy);
    }

}
