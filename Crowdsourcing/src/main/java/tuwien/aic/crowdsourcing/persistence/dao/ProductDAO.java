package tuwien.aic.crowdsourcing.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Product;

public interface ProductDAO extends JpaRepository<Product, Long> {

    Product findById(int id);
    
    Product findByName(String name);

}
