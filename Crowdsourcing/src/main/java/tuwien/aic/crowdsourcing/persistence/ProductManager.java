package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

public interface ProductManager {

    Product addProduct(String companyName,
                       String productName);
    
    void addProductSynonym(String companyName,
                           String productName,
                           String synonym);
    
    Product getProductByName(String companyName,
                             String productName);
    
    List<String> getProductNames(String companyName);
}
