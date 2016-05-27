package sopi.module.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.visit.model.DimDate;

@Repository
public interface DimDateRepository extends JpaRepository<DimDate, Long> {

}
