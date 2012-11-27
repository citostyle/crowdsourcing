
package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class ProductRating implements Serializable {

    private static final long serialVersionUID = -8768336940282518893L;
    private Long id = null;
    private MWTask task = null;
    private Worker worker = null;
    private Product product = null;
    private Date lastModified = null;
    private Integer ratingValue = null;

    public ProductRating() {
        this.lastModified = new Date();
    }

    public ProductRating(MWTask task,
            Product product,
            Integer ratingValue, Date lastModified) {
        this.task = task;
        this.product = product;
        this.ratingValue = ratingValue;
        this.lastModified = lastModified;
    }

    public ProductRating(MWTask task,
            Worker worker,
            Product product,
            Date lastModified,
            Integer ratingValue) {
        this.task = task;
        this.worker = worker;
        this.product = product;
        this.ratingValue = ratingValue;
        this.lastModified = lastModified;
    }

    @ManyToOne(optional = false)
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
    }

    @ManyToOne(optional = true)
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @ManyToOne(optional = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Column(nullable = true)
    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
