package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import tuwien.aic.crowdsourcing.persistence.entities.Product;

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
