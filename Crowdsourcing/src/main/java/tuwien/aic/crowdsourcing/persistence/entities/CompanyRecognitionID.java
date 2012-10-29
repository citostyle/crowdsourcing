package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompanyRecognitionID implements Serializable {
    
    private static final long serialVersionUID = -3789675089492370190L;
    
    private long taskId = -1;
    private long workerId = -1;
    private long companyId = -1;
    
    public CompanyRecognitionID() {
        
    }
    
    public CompanyRecognitionID(long taskId, 
                                long workerId, 
                                long companyId) {
        
        this.taskId = taskId;
        this.workerId = workerId;
        this.companyId = companyId;
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
    
    @Column(name = "company_id")
    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
