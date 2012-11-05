package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = -9106325467524175657L;

    private long id = -1L;

    private String name = "";
    private Company company = null;
    private Set<String> synonyms = new HashSet<String>();

    public Product() {

    }

    public Product(Company company, String name) {
        this.name = name;
        this.synonyms = new HashSet<String>();
        this.company = company;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(optional = false)
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ElementCollection
    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        if (synonyms != null) {
            this.synonyms = synonyms;
        } else {
            this.synonyms = new HashSet<String>();
        }
    }
}
