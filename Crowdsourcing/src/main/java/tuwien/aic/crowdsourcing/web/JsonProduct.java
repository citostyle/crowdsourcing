
package tuwien.aic.crowdsourcing.web;

import java.util.Set;

import tuwien.aic.crowdsourcing.persistence.entities.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(value = {
        "name", "id", "companyId", "rating", "numRatings", "synonyms"
})
public class JsonProduct {
    @JsonIgnore
    private final Product product;

    private final Double rating;
    private final Long numRatings;

    public JsonProduct(Product product) {
        this(product, null, 0L);
    }

    public JsonProduct(Product product, Double rating, Long numRatings) {
        this.product = product;
        this.rating = rating;
        this.numRatings = numRatings;
    }

    public long getId() {
        return product.getId();
    }

    public String getName() {
        return product.getName();
    }

    public Set<String> getSynonyms() {
        return product.getSynonyms();
    }

    public long getCompanyId() {
        return product.getCompany().getId();
    }

    public Double getRating() {
        return rating;
    }

    public Long getNumRatings() {
        return numRatings;
    }
}
