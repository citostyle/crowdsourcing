package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import tuwien.aic.crowdsourcing.persistence.entities.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class JsonProduct {
	@JsonIgnore
	private final Product product;

	public JsonProduct(Product product) {
		this.product = product;
	}
	
	
	public long getId() {
		return product.getId();
	}
	
	public String getName() {
		return product.getName();
	}
	
	public Set<String> getSynonyms() {
		return product.getSynonyms();
	}
	
	public long getCompanyId() {
		return product.getCompany().getId();
	}
}
