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
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger(JsonList.class);
	
	@Autowired
	@JsonIgnore
	private CompanyManager companyManager;
	
	@Autowired
	@JsonIgnore
	private ProductManager productManager;
	
	@JsonIgnore
	private final boolean showCompanies;
	@JsonIgnore
	private final boolean showProducts;
	
	public JsonList(boolean showCompanies, boolean showProducts) {
		this.showCompanies = showCompanies;
		this.showProducts = showProducts;
	}
	
	/**
	 * Returns a list of all Companies as json-wrapper-objects, if showCompanies is true 
	 */
	public List<JsonCompany> getCompanies() {
		if(!showCompanies)
			return null;
		
		List<JsonCompany> list = new LinkedList<JsonCompany>();
		
		for(Company company : companyManager.findAll())
		{
			list.add(new JsonCompany(company));
		}
		
		return list;
		/*
        Company company = new Company();
        company.setName("Foobar");
        company.setId(25L);
        company.getSynonyms().add("foo");
        company.getSynonyms().add("bar");
        company.getSynonyms().add("baz");
        
        JsonCompany c = new JsonCompany(company);
        List<JsonCompany> l = new LinkedList<JsonCompany>();
        l.add(c);
        return l;*/
	}
	
	/**
	 * Returns a list of all Products as json-wrapper-objects, if showProducts is true 
	 */
	public List<JsonProduct> getProducts() {
		if(!showProducts)
			return null;
		
		List<JsonProduct> list = new LinkedList<JsonProduct>();
		
		for(Product product : productManager.findAll())
			list.add(new JsonProduct(product));
		
		return list;
		/*
        Company company = new Company();
        company.setName("Foobar");
        company.setId(25L);
        company.getSynonyms().add("foo");
        company.getSynonyms().add("bar");
        company.getSynonyms().add("baz");
        
        Product product = new Product();
		product.setId(5);
		product.setName("Fenster");
		product.getSynonyms().add("Windoze");
		product.setCompany(company);
        
		JsonProduct p = new JsonProduct(product);
		
        List<JsonProduct> l = new LinkedList<JsonProduct>();
        l.add(p);
        return l;
        */
	}
}
