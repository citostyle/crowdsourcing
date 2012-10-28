package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class MWTask implements Serializable {

    private Long id = -1L;

    private String taskId = "";

    private String type = "";

    private String state = "";
    
    private Article article = null;

    public MWTask() {
        
    }
    
    public MWTask(Article article, 
                  Long id, String taskId, 
                  String type, String state) {
        this.id = id;
        this.type = type;
        this.state = state;
        this.taskId = taskId;
        this.article = article;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskState() {
        return state;
    }

    public void setTaskState(String state) {
        this.state = state;
    }
    
    @ManyToOne(optional=false)
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
