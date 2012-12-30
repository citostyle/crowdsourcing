package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProductRatingIndividual implements Serializable {

    private static final long serialVersionUID = 6611912415000944132L;

    private Long id = null;
    private ProductRating rating = null;
    private Integer ratingValue = null;
    private Integer timeTaken = null;
    private Worker worker = null;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @ManyToOne(optional = false)
    public ProductRating getRating() {
        return rating;
    }
    
    public void setRating(ProductRating rating) {
        this.rating = rating;
    }
    
    @Column(nullable = false)
    public Integer getRatingValue() {
        return ratingValue;
    }
    
    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
    
    @Column(nullable = false)
    public Integer getTimeTaken() {
        return timeTaken;
    }
    
    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }
    
    @ManyToOne(optional = false)
    public Worker getWorker() {
        return worker;
    }
    
    public void setWorker(Worker worker) {
        this.worker = worker;
    }
    
}
