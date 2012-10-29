package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class CompanyRecognition implements Serializable {
    
    private static final long serialVersionUID = -6794212226005929652L;
    
    private CompanyRecognitionID primaryKey = null;
    
    private MWTask task = null;
    
    private Worker worker = null;
    
    private Company company = null;
    
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
            new CompanyRecognitionID(task.getId(),
                                     worker.getId(),
                                     company.getId());
        
        this.task = task;
        this.worker = worker;
        this.company = company;
        
        this.lastModified = new Date();
    }
    
    public CompanyRecognition(MWTask task, 
                              Worker worker, 
                              Company company,
                              Date lastModified) {
        
        this.primaryKey = 
            new CompanyRecognitionID(task.getId(),
                                     worker.getId(),
                                     company.getId());
        
        this.task = task;
        this.worker = worker;
        this.company = company;
        
        this.lastModified = lastModified;
    }

    @EmbeddedId
    public CompanyRecognitionID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CompanyRecognitionID primaryKey) {
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
    @JoinColumn(name = "company", referencedColumnName = "id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
