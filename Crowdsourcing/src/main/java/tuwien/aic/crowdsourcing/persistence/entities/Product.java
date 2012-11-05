package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = -9106325467524175657L;
    
    private long id = -1L;

    private String name = "";
    
    private Company company = null;

    private Set<String> synonyms = null;
    
    public Product() {
        this.synonyms = 
            new HashSet<String>();
    }
    
    public Product(Company company, String name) {
        this.name = name;
        
        this.synonyms = 
            new HashSet<String>();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable=false)
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

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        if (synonyms != null) {
            this.synonyms = synonyms;
        }
        else {
            this.synonyms = 
                new HashSet<String>();
        }
    }
}
