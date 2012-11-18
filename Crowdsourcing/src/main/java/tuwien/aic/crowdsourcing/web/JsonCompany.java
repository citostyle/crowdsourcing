package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import tuwien.aic.crowdsourcing.persistence.entities.Company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(value={"name", "id", "synonyms"})
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
