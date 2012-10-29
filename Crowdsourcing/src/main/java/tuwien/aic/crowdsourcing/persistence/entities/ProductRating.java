package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@AssociationOverrides({
    @AssociationOverride(name = "primaryKey.task", 
            joinColumns = @JoinColumn(name = "TASK_ID")),
    @AssociationOverride(name = "primaryKey.worker", 
            joinColumns = @JoinColumn(name = "WORKER_ID")),
    @AssociationOverride(name = "primaryKey.product", 
            joinColumns = @JoinColumn(name = "PRODUCT_ID")) })
public class ProductRating implements Serializable {
    
    private static final long serialVersionUID = -8768336940282518893L;
    
    private ProductRatingID primaryKey = null;
    
    private MWTask task = null;
    
    private Worker worker = null;
    
    private Product product = null;
    
    private Date lastModified = null;
    
    private Integer ratingValue = null;
    
    public ProductRating() {
        this.primaryKey = 
            new ProductRatingID();
        
        this.lastModified = new Date();
    }
    
    public ProductRating(MWTask task, 
                         Worker worker, 
                         Product product,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new ProductRatingID(task.getId(),
                                worker.getId(),
                                product.getId());
        
        this.task = task;
        this.worker = worker;
        this.product = product;
        
        this.ratingValue = ratingValue;
        
        this.lastModified = new Date();
    }
    
    public ProductRating(MWTask task, 
                         Worker worker, 
                         Product product,
                         Date lastModified,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new ProductRatingID(task.getId(),
                                worker.getId(),
                                product.getId());
        
        this.task = task;
        this.worker = worker;
        this.product = product;
        
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
