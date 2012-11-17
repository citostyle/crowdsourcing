package tuwien.aic.crowdsourcing.rss;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.Product;

public class ArticleParserTest {

    @Mock
    private ProductManager productManager;

    @Mock
    private CompanyManager companyManager;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @InjectMocks
    private ArticleParser a = new ArticleParser();

    @Test
    public void testGetProductsInArticle() throws IOException {
        Product p1 = new Product(null, "Exchange");
        Product p2 = new Product(null, "Exchangeasdfasdfasdf");
        Mockito.when(productManager.findAll())
                .thenReturn(Arrays.asList(p1, p2));
        List<Product> ret =
                a.getProductsInArticle("http://finance.yah"
                        + "oo.com/news/why-couldnt-wall"
                        + "-street-weather-054400413.html");
        assertTrue(ret.contains(p1));
        assertFalse(ret.contains(p2));
    }

    @Test
    public void testGetCompaniesInArticle() throws IOException {
        Company p1 = new Company("Exchange");
        Company p2 = new Company("Exchangeasdfasdfasdf");
        Mockito.when(companyManager.findAll())
                .thenReturn(Arrays.asList(p1, p2));
        List<Company> ret =
                a.getCompaniesInArticle("http://finance.yah"
                        + "oo.com/news/why-couldnt-wall"
                        + "-street-weather-054400413.html");
        assertTrue(ret.contains(p1));
        assertFalse(ret.contains(p2));
    }
}
