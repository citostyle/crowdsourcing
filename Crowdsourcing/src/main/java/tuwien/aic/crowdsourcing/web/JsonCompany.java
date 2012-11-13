package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import tuwien.aic.crowdsourcing.persistence.entities.Company;


public class JsonCompany {
	@JsonIgnore
	private final Company company;
	
	public JsonCompany(Company company) {
		this.company = company;
	}
	
	
	public long getId() {
		return company.getId();
	}
	
	public String getName() {
		return company.getName();
	}
	
	public Set<String> getSynonyms() {
		return company.getSynonyms();
	}
}
