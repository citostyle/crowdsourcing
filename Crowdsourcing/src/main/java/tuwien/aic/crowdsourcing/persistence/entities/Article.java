package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Article implements Serializable {

    private long id = -1L;
    
    private String guid = "";

    private String title = "";

    private String address = "";
    
    private Set<MWTask> tasks = null;

    public Article() {
        this.tasks = new HashSet<MWTask>();
    }
    
    public Article(String guid, String title, String address) {
        this.guid = guid;
        this.title = title;
        this.address = address;
        
        this.tasks = new HashSet<MWTask>();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique=true, nullable=false)
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Column(nullable=false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(unique=true, nullable=false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    @OneToMany(mappedBy = "article")
    public Set<MWTask> getTasks() {
        return tasks;
    }

    public void setMemberships(Set<MWTask> tasks) {
        if (tasks != null) {
            this.tasks = tasks;
        }
        else {
            this.tasks = new HashSet<MWTask>();
        }
    }    
}
