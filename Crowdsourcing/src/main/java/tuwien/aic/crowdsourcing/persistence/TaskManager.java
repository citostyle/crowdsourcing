package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

public interface TaskManager {

    List<MWTask> getActiveTasks();

    public int getResponseCount(String taskId);

    public void setTaskState(String taskId, TaskState state);
}
