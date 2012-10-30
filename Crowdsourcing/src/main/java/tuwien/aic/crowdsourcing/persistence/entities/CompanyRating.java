package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class CompanyRating implements Serializable {
    
    private static final long serialVersionUID = 2596270620759191152L;
    
    private CompanyRatingID primaryKey = null;
    
    private MWTask task = null;
    
    private Worker worker = null;
    
    private Company company = null;
    
    private Date lastModified = null;
    
    private Integer ratingValue = null;
    
    public CompanyRating() {
        this.primaryKey = 
            new CompanyRatingID();
        
        this.lastModified = new Date();
    }
    
    public CompanyRating(MWTask task, 
                         Worker worker, 
                         Company company,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new CompanyRatingID(task.getId(),
                                worker.getId(),
                                company.getId());
        
        this.task = task;
        this.worker = worker;
        this.company = company;
        
        this.ratingValue = ratingValue;
        
        this.lastModified = new Date();
    }
    
    public CompanyRating(MWTask task, 
                         Worker worker, 
                         Company company,
                         Date lastModified,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new CompanyRatingID(task.getId(),
                                worker.getId(),
                                company.getId());
        
        this.task = task;
        this.worker = worker;
        this.company = company;
        
        this.ratingValue = ratingValue;
        
        this.lastModified = lastModified;
    }

    @EmbeddedId
    public CompanyRatingID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CompanyRatingID primaryKey) {
        this.primaryKey = primaryKey;
    }

    @ManyToOne(optional = false)
    @MapsId("taskId")
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
    }

    @ManyToOne(optional = false)
    @MapsId("workerId")
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @ManyToOne(optional = false)
    @MapsId("companyId")
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

    @Column(nullable=true)
    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
}