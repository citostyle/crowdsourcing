
package tuwien.aic.crowdsourcing.web;

import java.util.Date;

import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(value = {
        "id", "rating", "lastModified"
})
public class JsonRating {
    @JsonIgnore
    private final CompanyRating companyRating;
    @JsonIgnore
    private final ProductRating productRating;

    public JsonRating(CompanyRating rating) {
        companyRating = rating;
        productRating = null;
    }
    
    public JsonRating(ProductRating rating) {
        companyRating = null;
        productRating = rating;
    }

    public long getId() {
        if (companyRating != null)
            return companyRating.getId();
        return productRating.getId();
    }

    public int getRating() {
        if (companyRating != null)
            return companyRating.getRatingValue();
        return productRating.getRatingValue();
    }

    public Date getLastModified() {
        if (companyRating != null)
            return companyRating.getLastModified();
        return productRating.getLastModified();
    }
}
