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
public class CompanyRating implements Serializable {
    
    private CompanyRatingID primaryKey = null;
    
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
            new CompanyRatingID(task,
                                worker,
                                company);
        
        this.ratingValue = ratingValue;
        
        this.lastModified = new Date();
    }
    
    public CompanyRating(MWTask task, 
                         Worker worker, 
                         Company company,
                         Date lastModified,
                         Integer ratingValue) {
        
        this.primaryKey = 
            new CompanyRatingID(task,
                                worker,
                                company);
        
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

    @Column(nullable=true)
    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
}
