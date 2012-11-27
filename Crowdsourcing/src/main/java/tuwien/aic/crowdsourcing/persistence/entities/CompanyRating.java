
package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class CompanyRating implements Serializable {

    private static final long serialVersionUID = 2596270620759191152L;
    private Long id = null;
    private MWTask task = null;
    private Worker worker = null;
    private Company company = null;
    private Date lastModified = null;
    private Integer ratingValue = null;

    public CompanyRating() {
        this.lastModified = new Date();
    }

    public CompanyRating(MWTask task,
            Company company,
            Integer ratingValue, Date lastModified) {
        this.task = task;
        this.company = company;
        this.ratingValue = ratingValue;
        this.lastModified = lastModified;
    }

    public CompanyRating(MWTask task,
            Worker worker,
            Company company,
            Date lastModified,
            Integer ratingValue) {
        this.task = task;
        this.worker = worker;
        this.company = company;
        this.ratingValue = ratingValue;
        this.lastModified = lastModified;
    }

    @ManyToOne(optional = false)
    public MWTask getTask() {
        return task;
    }

    public void setTask(MWTask task) {
        this.task = task;
    }

    @ManyToOne(optional = true)
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

    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Column(nullable = true)
    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
