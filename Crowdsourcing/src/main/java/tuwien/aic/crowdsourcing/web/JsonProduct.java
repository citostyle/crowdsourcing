package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import tuwien.aic.crowdsourcing.persistence.entities.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(value={"name","id", "companyId", "rating", "synonyms"})
public class JsonProduct {
	@JsonIgnore
	private final Product product;
	
	private final Double rating;

	public JsonProduct(Product product) {
		this(product, null);
	}
	
	public JsonProduct(Product product, Double rating) {
		this.product = product;
		this.rating = rating;
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
	
	public Double getRating() {
		return rating;
	}
}
