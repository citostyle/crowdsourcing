package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Company implements Serializable {

    private static final long serialVersionUID = -5218547723818900411L;
    
    private long id = -1L;

    private String name = "";
    
    private Set<String> synonyms = null;
    
    private List<Product> products = null;

    public Company() {
        this.synonyms = 
            new HashSet<String>();
    }
    
    public Company(String name) {
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

    @Column(unique=true, nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "company")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    @ElementCollection
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
