package tuwien.aic.crowdsourcing.web;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@JsonSerialize(include=Inclusion.NON_NULL)
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
