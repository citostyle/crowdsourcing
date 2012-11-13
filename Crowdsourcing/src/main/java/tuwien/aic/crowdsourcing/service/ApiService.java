package tuwien.aic.crowdsourcing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;

@Service
public class ApiService {

	@Autowired
	private CompanyManager companyManager;
	
	@Transactional
	public List<Company> searchCompany(String expression)
	{
		ArrayList<Company> searchResults = new ArrayList<Company>();
		
		// Dirty, dirty rewrap hack to circumvent Jackson/Hibernate lazy loading problems
		for(Company company : companyManager.findByNameContainingIgnoreCase(expression))
			searchResults.add(rewrapCompany(company));
		
		for(Company company : companyManager.findBySynonymsContainingIgnoreCase(expression))
			searchResults.add(rewrapCompany(company));
		
		return searchResults;
	}

	private Company rewrapCompany(Company company) {
		
		Company newCompany = new Company();
		
		long id = company.getId();
		String name = company.getName();
		Set<String> synonyms = company.getSynonyms();
		
		newCompany.setId(id);
		newCompany.setName(name);
		newCompany.setSynonyms(synonyms);
		
		return newCompany;
	}
}
