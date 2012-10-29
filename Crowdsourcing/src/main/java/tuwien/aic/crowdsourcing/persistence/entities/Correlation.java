package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class Correlation implements Serializable {
    
    private static final long serialVersionUID = -7126095580813016601L;
    
    private CorrelationID primaryKey = null;
    
    private MWTask task = null;
    
    private Worker worker = null;
    
    private Company company = null;
    
    private Product product = null;
    
    private Date lastModified = null;
    
    public Correlation() {
        this.primaryKey = 
            new CorrelationID();
        
        this.lastModified = new Date();
    }
    
    public Correlation(MWTask task, 
                       Worker worker, 
                       Company company,
                       Product product) {
        
        this.primaryKey = 
            new CorrelationID(task.getId(),
                              worker.getId(),
                              company.getId(),
                              product.getId());
        
        this.task = task;
        this.worker = worker;
        this.company = company;
        this.product = product;
        
        this.lastModified = new Date();
    }
    
    public Correlation(MWTask task, 
                       Worker worker, 
                       Company company,
                       Product product,
                       Date lastModified) {
        
        this.primaryKey = 
            new CorrelationID(task.getId(),
                              worker.getId(),
                              company.getId(),
                              product.getId());
        
        this.task = task;
        this.worker = worker;
        this.company = company;
        this.product = product;
        
        this.lastModified = lastModified;
    }

    @EmbeddedId
    public CorrelationID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CorrelationID primaryKey) {
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
