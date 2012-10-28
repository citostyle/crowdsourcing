package tuwien.aic.crowdsourcing.persistence.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;

public interface MWTaskDAO extends JpaRepository<MWTask, Long> {

    MWTask findById(int id);
    
    MWTask findByTaskId(String taskId);
    
    Set<MWTask> findByState(String state);

}
