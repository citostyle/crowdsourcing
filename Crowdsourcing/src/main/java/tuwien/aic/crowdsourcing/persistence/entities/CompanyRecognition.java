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
    
    public CompanyRecognition(MWTask mwTask, 
                              Worker worker, 
                              Company company) {
        
        this.primaryKey = 
            new CompanyRecognitionID(mwTask,
                                     worker,
                                     company);
        
        this.lastModified = new Date();
    }
    
    public CompanyRecognition(MWTask mwTask, 
                              Worker worker, 
                              Company company,
                              Date lastModified) {
        
        this.primaryKey = 
            new CompanyRecognitionID(mwTask,
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
    public Company getCompany() {
        return primaryKey.getCompany();
    }

    public void setCompany(Company company) {
        primaryKey.setProduct(company);
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
