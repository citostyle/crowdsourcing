package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import tuwien.aic.crowdsourcing.persistence.entities.Company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
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
