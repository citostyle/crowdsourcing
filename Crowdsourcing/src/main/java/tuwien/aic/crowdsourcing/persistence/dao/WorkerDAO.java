package tuwien.aic.crowdsourcing.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface WorkerDAO extends JpaRepository<Worker, Long> {

    Worker findById(int id);
    
    Worker findByWorkerId(String workerId);

}
