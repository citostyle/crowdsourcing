
package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Article implements Serializable {

    private static final long serialVersionUID = -8943591096275220135L;
    private long id = -1L;
    private String title = "";
    private String address = "";
    private Set<MWTask> tasks = null;

    public Article() {
        this.tasks = new HashSet<MWTask>();
    }

    public Article(String title, String address) {
        this.title = title;
        this.address = address;

        this.tasks = new HashSet<MWTask>();
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(unique = true, nullable = false)
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

    public void setTasks(Set<MWTask> tasks) {
        if (tasks != null) {
            this.tasks = tasks;
        }
        else {
            this.tasks = new HashSet<MWTask>();
        }
    }
}
