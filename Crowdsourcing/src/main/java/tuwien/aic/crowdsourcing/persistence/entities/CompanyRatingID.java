package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class CompanyRatingID implements Serializable {
    private MWTask mwTask = null;
    private Worker worker = null;
    private Company company = null;
    
    public CompanyRatingID() {
        
    }
    
    public CompanyRatingID(MWTask mwTask, 
                           Worker worker, 
                           Company company) {
        
        this.mwTask = mwTask;
        this.worker = worker;
        this.company = company;
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

    public void setProduct(Company company) {
        this.company = company;
    }
    
}
