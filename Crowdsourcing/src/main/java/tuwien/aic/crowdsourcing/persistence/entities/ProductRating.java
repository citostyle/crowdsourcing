package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.task", joinColumns = @JoinColumn(name = "TASK_ID")),
        @AssociationOverride(name = "primaryKey.worker", joinColumns = @JoinColumn(name = "WORKER_ID")),
        @AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "PRODUCT_ID")) })
public class ProductRating implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8768336940282518893L;

    private ProductRatingID primaryKey = null;

    private Date lastModified = null;

    private Integer ratingValue = null;

    public ProductRating() {
        this.primaryKey = new ProductRatingID();

        this.lastModified = new Date();
    }

    public ProductRating(MWTask task, Worker worker, Product product,
            Integer ratingValue) {

        this.primaryKey = new ProductRatingID(task, worker, product);

        this.ratingValue = ratingValue;

        this.lastModified = new Date();
    }

    public ProductRating(MWTask task, Worker worker, Product product,
            Date lastModified, Integer ratingValue) {

        this.primaryKey = new ProductRatingID(task, worker, product);

        this.ratingValue = ratingValue;

        this.lastModified = lastModified;
    }

    @EmbeddedId
    public ProductRatingID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ProductRatingID primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Column(nullable = false)
    @Transient
    public MWTask getTask() {
        return primaryKey.getTask();
    }

    public void setTask(MWTask task) {
        primaryKey.setTask(task);
    }

    @Column(nullable = false)
    @Transient
    public Worker getWorker() {
        return primaryKey.getWorker();
    }

    public void setWorker(Worker worker) {
        primaryKey.setWorker(worker);
    }

    @Column(nullable = false)
    @Transient
    public Product getProduct() {
        return primaryKey.getProduct();
    }

    public void setProduct(Product product) {
        primaryKey.setProduct(product);
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
}
