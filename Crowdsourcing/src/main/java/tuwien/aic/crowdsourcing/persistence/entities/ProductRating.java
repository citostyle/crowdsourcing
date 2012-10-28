package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@AssociationOverrides({
    @AssociationOverride(name = "primaryKey.mwTask", 
            joinColumns = @JoinColumn(name = "TASK_ID")),
    @AssociationOverride(name = "primaryKey.worker", 
            joinColumns = @JoinColumn(name = "WORKER_ID")),
    @AssociationOverride(name = "primaryKey.product", 
            joinColumns = @JoinColumn(name = "PRODUCT_ID")) })
public class ProductRating implements Serializable {
    
    private ProductRatingID primaryKey = null;
    
    private Date lastModified = null;
    
    private Integer ratingValue = null;
    
    public ProductRating() {
        this.primaryKey = 
            new ProductRatingID();
        
        this.lastModified = new Date();
    }
    
    public ProductRating(MWTask mwTask, 
                         Worker worker, 
                         Product product,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new ProductRatingID(mwTask,
                                worker,
                                product);
        
        this.ratingValue = ratingValue;
        
        this.lastModified = new Date();
    }
    
    public ProductRating(MWTask mwTask, 
                         Worker worker, 
                         Product product,
                         Date lastModified,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new ProductRatingID(mwTask,
                                worker,
                                product);
        
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

    @Transient
    public MWTask getMWTask() {
        return primaryKey.getMWTask();
    }

    public void setMWTask(MWTask mwTask) {
        primaryKey.setMWTask(mwTask);
    }

    @Transient
    public Worker getWorker() {
        return primaryKey.getWorker();
    }

    public void setWorker(Worker worker) {
        primaryKey.setWorker(worker);
    }

    @Transient
    public Product getProduct() {
        return primaryKey.getProduct();
    }

    public void setProduct(Product product) {
        primaryKey.setProduct(product);
    }

    @Column(nullable=false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Column(nullable=true)
    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
    
}
