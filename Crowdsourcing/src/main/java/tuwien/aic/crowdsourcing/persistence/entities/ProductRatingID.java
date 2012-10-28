package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ProductRatingID implements Serializable {
    private MWTask mwTask = null;
    private Worker worker = null;
    private Product product = null;
    
    public ProductRatingID() {
        
    }
    
    public ProductRatingID(MWTask mwTask, 
                           Worker worker, 
                           Product product) {
        
        this.mwTask = mwTask;
        this.worker = worker;
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
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
