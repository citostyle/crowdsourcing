package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CorrelationID implements Serializable {
    
    private static final long serialVersionUID = -2387168922360511801L;
    
    private long taskId = -1;
    private long workerId = -1;
    private long companyId = -1;
    private long productId = -1;
    
    public CorrelationID() {
        
    }
    
    public CorrelationID(long taskId, 
                         long workerId, 
                         long companyId,
                         long productId) {
        
        this.taskId = taskId;
        this.workerId = workerId;
        this.companyId = companyId;
        this.productId = productId;
    }

    @Column(name = "worker_id")
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

    @Column(name = "company_id")
    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    @Column(name = "product_id")
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
