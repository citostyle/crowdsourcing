package tuwien.aic.crowdsourcing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.Worker;

public interface WorkerManager extends JpaRepository<Worker, Long> {

    Worker findByWorkerId(String workerId);

}
