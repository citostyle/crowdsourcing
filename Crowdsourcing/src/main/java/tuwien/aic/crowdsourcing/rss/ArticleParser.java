package tuwien.aic.crowdsourcing.rss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Service
public class ArticleParser {

    private static final String YAHOO_CONTENT = "yom-mod yom-art-content";

    @Autowired
    private ProductManager productManager;

    @Autowired
    private CompanyManager companyManager;

    @Transactional
    private boolean foundProduct(String text, Product product) {
        String lower = text.toLowerCase();
        if (lower.contains(product.getName().toLowerCase())) {
            return true;
        }
        for (String syn : product.getSynonyms()) {
            if (lower.contains(syn.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param url
     * @param exceptions
     *            Must be lowercase. Won't be reported.
     * @return
     * @throws IOException
     */
    @Transactional
    public List<Product> getProductsInArticle(Document document) {
        // System.out.println("Trying to parse products in " + url);
        List<Product> allProducts = productManager.findAll();

        List<Product> ret = new ArrayList<Product>();
        String text = document.body().text();
        for (Product p : allProducts) {
            if (foundProduct(text, p)) {
                ret.add(p);
            }
        }
        return ret;
    }

    @Transactional
    private boolean foundCompany(String text, Company company) {
        String lower = text.toLowerCase();
        if (lower.contains(company.getName().toLowerCase())) {
            return true;
        }
        for (String syn : company.getSynonyms()) {
            if (lower.contains(syn.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param url
     * @param exceptions
     *            Must be lowercase.
     * @return
     * @throws IOException
     */
    @Transactional
    public List<Company> getCompaniesInArticle(Document document) {
        // System.out.println("Trying to parse companies in " + url);
        List<Company> allProducts = companyManager.findAll();

        List<Company> ret = new ArrayList<Company>();
        document.getElementsByClass(YAHOO_CONTENT);
        String text = document.body().text();
        for (Company p : allProducts) {
            if (foundCompany(text, p)) {
                ret.add(p);
            }
        }
        return ret;
    }

    public Document readArticle(String url) throws IOException {
        return Jsoup.connect(url).timeout(10 * 1000).get();
    }
}
