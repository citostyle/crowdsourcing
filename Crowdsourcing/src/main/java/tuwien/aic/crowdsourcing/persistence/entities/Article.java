package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Article implements Serializable {

    private Long id = -1L;
    
    private String guid = "";

    private String title = "";

    private String url = "";
    
    private Set<MWTask> tasks = null;

    public Article() {
        this.tasks = new HashSet<MWTask>();
    }
    
    public Article(Long id, String guid, String title, String url) {
        this.id = id;
        this.url = url;
        this.guid = guid;
        this.title = title;
        
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
