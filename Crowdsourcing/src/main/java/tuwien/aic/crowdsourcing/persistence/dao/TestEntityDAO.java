
package tuwien.aic.crowdsourcing.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tuwien.aic.crowdsourcing.persistence.entities.TestEntity;

public interface TestEntityDAO extends JpaRepository<TestEntity, Long> {

}
