package tuwien.aic.crowdsourcing.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class JsonList {	
	private final List<JsonCompany> companies;
	private final List<JsonProduct> products;
	
	public JsonList(List<JsonCompany> companies, List<JsonProduct> products) {
		this.companies = companies;
		this.products = products;
	}
	
	public List<JsonCompany> getCompanies() {
		return companies;
	}

	public List<JsonProduct> getProducts() {
		return products;
	}
}
