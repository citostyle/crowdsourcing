package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.task", joinColumns = @JoinColumn(name = "TASK_ID")),
        @AssociationOverride(name = "primaryKey.worker", joinColumns = @JoinColumn(name = "WORKER_ID")),
        @AssociationOverride(name = "primaryKey.company", joinColumns = @JoinColumn(name = "COMPANY_ID")),
        @AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "PRODUCT_ID")) })
public class Correlation implements Serializable {

    /**
     *  
     */
    private static final long serialVersionUID = -7126095580813016601L;

    private CorrelationID primaryKey = null;

    private Date lastModified = null;

    public Correlation() {
        this.primaryKey = new CorrelationID();

        this.lastModified = new Date();
    }

    public Correlation(MWTask task, Worker worker, Company company,
            Product product) {

        this.primaryKey = new CorrelationID(task, worker, company, product);

        this.lastModified = new Date();
    }

    public Correlation(MWTask task, Worker worker, Company company,
            Product product, Date lastModified) {

        this.primaryKey = new CorrelationID(task, worker, company, product);

        this.lastModified = lastModified;
    }

    @EmbeddedId
    public CorrelationID getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CorrelationID primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Column(nullable = false)
    @Transient
    public MWTask getTask() {
        return primaryKey.getTask();
    }

    public void setTask(MWTask task) {
        primaryKey.setTask(task);
    }

    @Column(nullable = false)
    @Transient
    public Worker getWorker() {
        return primaryKey.getWorker();
    }

    public void setWorker(Worker worker) {
        primaryKey.setWorker(worker);
    }

    @Column(nullable = false)
    @Transient
    public Company getCompany() {
        return primaryKey.getCompany();
    }

    public void setCompany(Company company) {
        primaryKey.setCompany(company);
    }

    @Column(nullable = false)
    @Transient
    public Product getProduct() {
        return primaryKey.getProduct();
    }

    public void setProduct(Product product) {
        primaryKey.setProduct(product);
    }

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
