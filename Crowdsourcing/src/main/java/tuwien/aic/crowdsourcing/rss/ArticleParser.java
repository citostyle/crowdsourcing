package tuwien.aic.crowdsourcing.rss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

@Component
public class ArticleParser {

    @Autowired
    private ProductManager productManager;

    @Autowired
    private CompanyManager companyManager;

    private boolean foundProduct(String text, Product product,
            List<String> exceptions) {
        String lower = text.toLowerCase();
        if (!exceptions.contains(product.getName().toLowerCase())) {
            if (lower.contains(product.getName().toLowerCase())) {
                return true;
            }
        }
        for (String syn : product.getSynonyms()) {
            if (!exceptions.contains(syn.toLowerCase())) {
                if (lower.contains(syn.toLowerCase())) {
                    return true;
                }
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
    public List<Product> getProductsInArticle(String url,
            List<String> exceptions) throws IOException {
        List<Product> allProducts = productManager.findAll();

        Document document = Jsoup.connect(url).get();
        List<Product> ret = new ArrayList<Product>();
        String text = document.body().text();
        for (Product p : allProducts) {
            if (foundProduct(text, p, exceptions)) {
                ret.add(p);
            }
        }
        return ret;
    }

    private boolean foundCompany(String text, Company company,
            List<String> exceptions) {
        String lower = text.toLowerCase();
        if (!exceptions.contains(company.getName().toLowerCase())) {
            if (lower.contains(company.getName().toLowerCase())) {
                return true;
            }
        }
        for (String syn : company.getSynonyms()) {
            if (!exceptions.contains(syn.toLowerCase())) {
                if (lower.contains(syn.toLowerCase())) {
                    return true;
                }
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
    public List<Company> getCompaniesInArticle(String url,
            List<String> exceptions) throws IOException {
        List<Company> allProducts = companyManager.findAll();

        Document document = Jsoup.connect(url).get();
        List<Company> ret = new ArrayList<Company>();
        String text = document.body().text();
        for (Company p : allProducts) {
            if (foundCompany(text, p, exceptions)) {
                ret.add(p);
            }
        }
        return ret;
    }
}
