package tuwien.aic.crowdsourcing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Service
public class ApiService {

	@Autowired
	private CompanyManager companyManager;
	
	@Autowired
	private ProductManager productManager;
	
	@Transactional
	public List<Company> searchCompany(String expression)
	{
		ArrayList<Company> searchResults = new ArrayList<Company>();
		
		List<Company> matchingCompanies = companyManager.findByNameContainingIgnoreCase(expression);
		searchResults.addAll(matchingCompanies);
		
		List<Company> matchingCompaniesSynonyms = companyManager.findBySynonymsContainingIgnoreCase(expression);
		searchResults.addAll(matchingCompaniesSynonyms);
		
		return searchResults;
	}
	
	@Transactional
	public List<Product> searchProduct(String expression)
	{
		ArrayList<Product> searchResults = new ArrayList<Product>();
		
		List<Product> matchingCompanies = productManager.findByNameContainingIgnoreCase(expression);
		searchResults.addAll(matchingCompanies);
		
		List<Product> matchingCompaniesSynonyms = productManager.findBySynonymsContainingIgnoreCase(expression);
		searchResults.addAll(matchingCompaniesSynonyms);
		
		return searchResults;
	}
}
