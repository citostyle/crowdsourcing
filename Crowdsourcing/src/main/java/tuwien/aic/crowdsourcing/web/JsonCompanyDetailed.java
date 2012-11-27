
package tuwien.aic.crowdsourcing.web;

import java.util.List;

import tuwien.aic.crowdsourcing.persistence.entities.Company;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = {
        "name", "id", "rating", "numRatings", "synonyms", "products"
})
public class JsonCompanyDetailed extends JsonCompany {
    private final Double rating;
    private final Long numRatings;
    private final List<JsonProduct> products;

    public JsonCompanyDetailed(Company company, Double rating, Long numRatings,
            List<JsonProduct> products) {
        super(company);

        this.rating = rating;
        this.products = products;
        this.numRatings = numRatings;
    }

    public Double getRating() {
        return rating;
    }

    public List<JsonProduct> getProducts() {
        return products;
    }

    public Long getNumRatings() {
        return numRatings;
    }
}
