package tuwien.aic.crowdsourcing.persistence.entities;

public enum TaskState implements java.io.Serializable {
    ACTIVE(0), FINISHED(1);

    private final Integer value;  
    
    private TaskState(Integer value) {  
        this.value = value;  
    }  
    
    public Integer getValue() {  
        return value;  
    }  
}
