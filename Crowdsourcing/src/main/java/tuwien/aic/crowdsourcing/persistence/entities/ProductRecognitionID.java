package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ProductRecognitionID implements Serializable {
    
    private static final long serialVersionUID = 3947878984382514207L;
    
    private MWTask task = null;
    private Worker worker = null;
    private Product product = null;
    
    public ProductRecognitionID() {
        
    }
    
    public ProductRecognitionID(MWTask task, 
                                Worker worker, 
                                Product product) {
        
        this.task = task;
        this.worker = worker;
        this.product = product;
    }

    @ManyToOne(optional=false)
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
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
