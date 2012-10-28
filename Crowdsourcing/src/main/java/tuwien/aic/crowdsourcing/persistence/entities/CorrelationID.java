package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class CorrelationID implements Serializable {
    private MWTask mwTask = null;
    private Worker worker = null;
    private Company company = null;
    private Product product = null;
    
    public CorrelationID() {
        
    }
    
    public CorrelationID(MWTask mwTask, 
                         Worker worker, 
                         Company company,
                         Product product) {
        
        this.mwTask = mwTask;
        this.worker = worker;
        this.company = company;
        this.product = product;
    }

    @ManyToOne(optional=false)
    public MWTask getMWTask() {
        return mwTask;
    }

    public void setMWTask(MWTask mwTask) {
        this.mwTask = mwTask;
    }

    @ManyToOne(optional=false)
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @ManyToOne(optional=false)
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ManyToOne(optional=false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
