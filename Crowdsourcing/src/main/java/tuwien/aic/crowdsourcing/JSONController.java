
package tuwien.aic.crowdsourcing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import tuwien.aic.crowdsourcing.persistence.CompanyRatingManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.service.ApiService;
import tuwien.aic.crowdsourcing.service.CompanyRatingService;
import tuwien.aic.crowdsourcing.service.ProductRatingService;
import tuwien.aic.crowdsourcing.web.JsonCompany;
import tuwien.aic.crowdsourcing.web.JsonCompanyDetailed;
import tuwien.aic.crowdsourcing.web.JsonList;
import tuwien.aic.crowdsourcing.web.JsonProduct;

/**
 * Handles requests for the application home page.
 */
@Controller
public class JSONController {

    private static final Logger logger = LoggerFactory
            .getLogger(JSONController.class);

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductRatingService productRatingService;

    @Autowired
    private CompanyRatingService companyRatingService;

    @Autowired
    private ProductRatingManager productRatingManager;

    @Autowired
    private CompanyRatingManager companyRatingManager;

    @Autowired
    private ApiService apiService;

    /**
     * Returns a list of all companies
     */
    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonList getCompanyList() {
        logger.info("Generating JSON response: List of all companies");
        List<JsonCompany> list = new LinkedList<JsonCompany>();
        for (Company company : companyManager.findAll()) {
            list.add(new JsonCompany(company));
        }
        return new JsonList(list, null);
    }

    /**
     * Returns info for the specified company
     */
    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonCompanyDetailed getCompany(@PathVariable
    String id) {
        Long lid;
        try {
            lid = Long.valueOf(id);
        } catch (NumberFormatException e)
        {
            return null;
        }

        logger.info("Generating JSON response for company with id %d", lid);

        Company company;
        List<JsonProduct> products = new LinkedList<JsonProduct>();

        company = companyManager.findOne(lid);

        if (company == null) {
            return null;
        }

        Double rating = companyRatingService.getCompanySentiment(company);

        // jsonproducts, yay
        for (Product p : company.getProducts()) {
            products.add(new JsonProduct(p));
        }

        Long numRatings = companyRatingManager.getNumRatings(company);
        if (numRatings == null) {
            numRatings = 0L;
        }

        return new JsonCompanyDetailed(company, rating, numRatings, products);
    }

    /**
     * Returns a list of all products
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonList getProductList() {

        logger.info("Generating JSON response: List of all products");

        List<JsonProduct> list = new LinkedList<JsonProduct>();

        for (Product product : productManager.findAll()) {
            list.add(new JsonProduct(product));
        }

        return new JsonList(null, list);
    }

    /**
     * Returns info for the specified product
     */
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonProduct getProduct(@PathVariable
    String id) {
        Long lid;
        try {
            lid = Long.valueOf(id);
        } catch (NumberFormatException e)
        {
            return null;
        }
        logger.info("Generating JSON response for product with id %d", lid);
        Product product = productManager.findOne(lid);
        if (product == null) {
            return null;
        }
        Double rating = productRatingService.getProductSentiment(product);
        Long numRatings = productRatingManager.getNumRatings(product);
        if (numRatings == null) {
            numRatings = 0L;
        }

        return new JsonProduct(product, rating, numRatings);
    }

    /**
     * Returns a list of all companies and products
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonList getCompleteList() {

        logger.info("Generating JSON response: List of all companies and products");

        List<JsonCompany> listC = new LinkedList<JsonCompany>();
        List<JsonProduct> listP = new LinkedList<JsonProduct>();

        for (Company company : companyManager.findAll()) {
            listC.add(new JsonCompany(company));
        }

        for (Product product : productManager.findAll()) {
            listP.add(new JsonProduct(product));
        }

        return new JsonList(listC, listP);
    }

    /**
     * Search companies/products for the given expression.
     */
    @RequestMapping(value = "/search/{expr}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonList searchCompaniesAndProducts(@PathVariable
    String expr) {

        logger.info("Searching companies/prodcuts for: " + expr);

        ArrayList<JsonCompany> listC = new ArrayList<JsonCompany>();
        ArrayList<JsonProduct> listP = new ArrayList<JsonProduct>();

        List<Company> companies = apiService.searchCompany(expr);

        for (Company c : companies) {
            listC.add(new JsonCompany(c));
        }

        List<Product> products = apiService.searchProduct(expr);

        for (Product p : products) {
            listP.add(new JsonProduct(p));
        }

        return new JsonList(listC, listP);
    }

    /**
     * Search companies for the given expression.
     */
    @RequestMapping(value = "/search/{expr}/companies", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonList searchCompanies(@PathVariable
    String expr) {

        logger.info("Searching companies for: " + expr);

        ArrayList<JsonCompany> listC = new ArrayList<JsonCompany>();

        List<Company> companies = apiService.searchCompany(expr);

        for (Company c : companies) {
            listC.add(new JsonCompany(c));
        }

        return new JsonList(listC, null);
    }

    /**
     * Search products for the given expression.
     */
    @RequestMapping(value = "/search/{expr}/products", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JsonList searchProducts(@PathVariable
    String expr) {

        logger.info("Searching products for: " + expr);

        ArrayList<JsonProduct> listP = new ArrayList<JsonProduct>();

        List<Product> products = apiService.searchProduct(expr);

        for (Product p : products) {
            listP.add(new JsonProduct(p));
        }

        return new JsonList(null, listP);
    }
}
