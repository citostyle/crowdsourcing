package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class CompanyRatingID implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5351268870227073825L;
    private MWTask task = null;
    private Worker worker = null;
    private Company company = null;

    public CompanyRatingID() {

    }

    public CompanyRatingID(MWTask task, Worker worker, Company company) {

        this.task = task;
        this.worker = worker;
        this.company = company;
    }

    @ManyToOne(optional = false)
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
    }

    @ManyToOne(optional = false)
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @ManyToOne(optional = false)
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setProduct(Company company) {
        this.company = company;
    }
}
