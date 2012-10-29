package tuwien.aic.crowdsourcing.persistence;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

public interface TaskManager {
    
    public int getResponseCount(String taskId);
    
    public void setTaskState(String taskId, 
                             TaskState state);
}
