
package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

public interface ProductManager extends JpaRepository<Product, Long> {

    Product findByCompanyAndName(Company company, String productName);

    Product findByName(String productName);

    List<Product> findByCompany(Company company);

    List<Product> findByNameContainingIgnoreCase(String expression);

    List<Product> findBySynonymsContainingIgnoreCase(String expression);
}
