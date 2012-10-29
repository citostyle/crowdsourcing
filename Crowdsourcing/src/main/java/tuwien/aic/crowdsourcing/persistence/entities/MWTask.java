package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class MWTask implements Serializable {

    private static final long serialVersionUID = 626524682741823847L;
    
    private long id = -1L;

    private String taskId = "";

    private String type = "";

    private TaskState state = TaskState.ACTIVE;
    
    private Article article = null;

    public MWTask() {
        
    }
    
    public MWTask(Article article, String taskId, 
                  String type, TaskState state) {
        this.type = type;
        this.state = state;
        this.taskId = taskId;
        this.article = article;
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
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(nullable=false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(nullable=false)
    public TaskState getTaskState() {
        return state;
    }

    public void setTaskState(TaskState state) {
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
