package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductRatingID implements Serializable {
    
    private static final long serialVersionUID = -665381804429632456L;
    
    private long taskId = -1;
    private long workerId = -1;
    private long productId = -1;
    
    public ProductRatingID() {
        
    }
    
    public ProductRatingID(long taskId, 
                           long workerId, 
                           long productId) {
        
        this.taskId = taskId;
        this.workerId = workerId;
        this.productId = productId;
    }

    @Column(name = "task_id")
    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Column(name = "worker_id")
    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    @Column(name = "product_id")
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        
        hash = 61 * hash + (int) (this.taskId ^ (this.taskId >>> 32));
        hash = 61 * hash + (int) (this.workerId ^ (this.workerId >>> 32));
        hash = 61 * hash + (int) (this.productId ^ (this.productId >>> 32));
        
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        
        if (o instanceof ProductRatingID) {
            ProductRatingID other = (ProductRatingID)o;
            
            ret = this.taskId == other.getTaskId() &&
                  this.workerId == other.getWorkerId() &&
                  this.productId == other.getProductId();
        }
        
        return ret;
    }
}
