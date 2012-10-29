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
public class ProductRecognition implements Serializable {
    
    private ProductRecognitionID primaryKey = null;
    
    private Date lastModified = null;
    
    public ProductRecognition() {
        this.primaryKey = 
            new ProductRecognitionID();
        
        this.lastModified = new Date();
    }
    
    public ProductRecognition(MWTask task, 
                              Worker worker, 
                              Product product) {
        
        this.primaryKey = 
            new ProductRecognitionID(task,
                                     worker,
                                     product);
        
        this.lastModified = new Date();
    }
    
    public ProductRecognition(MWTask task, 
                              Worker worker, 
                              Product product,
                              Date lastModified) {
        
        this.primaryKey = 
            new ProductRecognitionID(task,
                                     worker,
                                     product);
        
        this.lastModified = lastModified;
    }

    @EmbeddedId
    public ProductRecognitionID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ProductRecognitionID primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Column(nullable=false)
    @Transient
    public MWTask getTask() {
        return primaryKey.getTask();
    }

    public void setTask(MWTask task) {
        primaryKey.setTask(task);
    }

    @Column(nullable=false)
    @Transient
    public Worker getWorker() {
        return primaryKey.getWorker();
    }

    public void setWorker(Worker worker) {
        primaryKey.setWorker(worker);
    }

    @Column(nullable=false)
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
}
