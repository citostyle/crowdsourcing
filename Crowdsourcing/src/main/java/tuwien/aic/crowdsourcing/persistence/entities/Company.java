package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Company implements Serializable {

    private static final long serialVersionUID = -5218547723818900411L;
    
    private long id = -1L;

    private String name = "";

    public Company() {
        
    }
    
    public Company(String name) {
        this.name = name;
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
}
