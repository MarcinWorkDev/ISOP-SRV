package sopi.module.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.visit.model.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

}
