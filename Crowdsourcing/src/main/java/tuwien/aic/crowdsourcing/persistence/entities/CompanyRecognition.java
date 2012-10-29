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
    @AssociationOverride(name = "primaryKey.company", 
            joinColumns = @JoinColumn(name = "COMPANY_ID")) })
public class CompanyRecognition implements Serializable {
    
    private CompanyRecognitionID primaryKey = null;
    
    private Date lastModified = null;
    
    public CompanyRecognition() {
        this.primaryKey = 
            new CompanyRecognitionID();
        
        this.lastModified = new Date();
    }
    
    public CompanyRecognition(MWTask task, 
                              Worker worker, 
                              Company company) {
        
        this.primaryKey = 
            new CompanyRecognitionID(task,
                                     worker,
                                     company);
        
        this.lastModified = new Date();
    }
    
    public CompanyRecognition(MWTask task, 
                              Worker worker, 
                              Company company,
                              Date lastModified) {
        
        this.primaryKey = 
            new CompanyRecognitionID(task,
                                     worker,
                                     company);
        
        this.lastModified = lastModified;
    }

    @EmbeddedId
    public CompanyRecognitionID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CompanyRecognitionID primaryKey) {
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
    public Company getProduct() {
        return primaryKey.getCompany();
    }

    public void setProduct(Company company) {
        primaryKey.setCompany(company);
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
