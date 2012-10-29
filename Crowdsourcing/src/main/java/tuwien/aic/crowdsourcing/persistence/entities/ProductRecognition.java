package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class ProductRecognition implements Serializable {
    
    private static final long serialVersionUID = -4609094723687381904L;
    
    private ProductRecognitionID primaryKey = null;
    
    private MWTask task = null;
    
    private Worker worker = null;
    
    private Product product = null;
    
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
            new ProductRecognitionID(task.getId(),
                                     worker.getId(),
                                     product.getId());
        
        this.task = task;
        this.worker = worker;
        this.product = product;
        
        this.lastModified = new Date();
    }
    
    public ProductRecognition(MWTask task, 
                              Worker worker, 
                              Product product,
                              Date lastModified) {
        
        this.primaryKey = 
            new ProductRecognitionID(task.getId(),
                                     worker.getId(),
                                     product.getId());
        
        this.task = task;
        this.worker = worker;
        this.product = product;
        
        this.lastModified = lastModified;
    }

    @EmbeddedId
    public ProductRecognitionID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ProductRecognitionID primaryKey) {
        this.primaryKey = primaryKey;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "task", referencedColumnName = "id")
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker", referencedColumnName = "id")
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "product", referencedColumnName = "id")
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
}
