package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompanyRatingID implements Serializable {
    
    private static final long serialVersionUID = 5351268870227073825L;
    
    private long taskId = -1;
    private long workerId = -1;
    private long companyId = -1;
    
    public CompanyRatingID() {
        
    }
    
    public CompanyRatingID(long taskId, 
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

    @Override
    public int hashCode() {
        int hash = 7;
        
        hash = 61 * hash + (int) (this.taskId ^ (this.taskId >>> 32));
        hash = 61 * hash + (int) (this.workerId ^ (this.workerId >>> 32));
        hash = 61 * hash + (int) (this.companyId ^ (this.companyId >>> 32));
        
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        
        if (o instanceof CompanyRatingID) {
            CompanyRatingID other = (CompanyRatingID)o;
            
            ret = this.taskId == other.getTaskId() &&
                  this.workerId == other.getWorkerId() &&
                  this.companyId == other.getCompanyId();
        }
        
        return ret;
    }
}
