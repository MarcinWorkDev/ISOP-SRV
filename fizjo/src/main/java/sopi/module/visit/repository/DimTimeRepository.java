package sopi.module.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.visit.model.DimTime;

@Repository
public interface DimTimeRepository extends JpaRepository<DimTime, Long> {

}
