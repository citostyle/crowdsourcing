package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

public interface TaskManager extends JpaRepository<MWTask, Long> {

    List<MWTask> findByTaskState(TaskState state);

    MWTask findByTaskId(String taskId);
}
