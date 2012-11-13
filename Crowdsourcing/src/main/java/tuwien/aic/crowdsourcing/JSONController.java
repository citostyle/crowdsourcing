package tuwien.aic.crowdsourcing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.TestEntity;
import tuwien.aic.crowdsourcing.service.ApiService;
import tuwien.aic.crowdsourcing.web.JsonCompany;
import tuwien.aic.crowdsourcing.web.JsonList;

/**
 * Handles requests for the application home page.
 */
@Controller
public class JSONController {

    private static final Logger logger = LoggerFactory
            .getLogger(JSONController.class);
    
    @Autowired
    private ApiService apiService;
    

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/json/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TestEntity get(@PathVariable String id) {

        logger.info("Generating JSON response for id " + id);

        TestEntity testEntity = new TestEntity();

        testEntity.setName(id);
        try {
            testEntity.setId(Long.valueOf(id));
        } catch (NumberFormatException e) {
        }

        return testEntity;
    }
    
    
    /**
     * Returns a list of all companies
     */
    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @ResponseBody
    public JsonList getCompanyList() {

        logger.info("Generating JSON response: List of all companies");

        
        return new JsonList(true, false);
    }
    
    /**
     * Returns a list of all products
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @ResponseBody
    public JsonList getProductList() {

        logger.info("Generating JSON response: List of all products");

        
        return new JsonList(false, true);
    }
    
    /**
     * Returns a list of all companies and products
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public JsonList getCompleteList() {

        logger.info("Generating JSON response: List of all companies and products");

        
        return new JsonList(true, true);
    }
    
    /**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/search/{expr}", method = RequestMethod.GET)
	@ResponseBody
	public List<JsonCompany> searchCompanies(@PathVariable String expr) {
		
		logger.info("Searching for: " + expr);
		
		ArrayList<JsonCompany> result = new ArrayList<JsonCompany>();

		List<Company> companies = apiService.searchCompany(expr);
		
		for(Company c : companies)
		{
			JsonCompany company = new JsonCompany(c);
			result.add(company);
		}
		
		return result;
	}
}
