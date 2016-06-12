package sopi.module.visit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.visit.model.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
	
	List<Visit> findAllByProfileProfileId(Long profileId);
	List<Visit> findAllByScheduleProfileProfileId(Long profileId);

	List<Visit> findAllByProfileProfileIdOrderByScheduleDateDateId(Long profileId);
	List<Visit> findAllByScheduleProfileProfileIdOrderByScheduleDateDateId(Long profileId);
	
	List<Visit> findAllByProfileProfileIdOrderByScheduleDateDateIdDesc(Long profileId);
	List<Visit> findAllByScheduleProfileProfileIdOrderByScheduleDateDateIdDesc(Long profileId);
}
