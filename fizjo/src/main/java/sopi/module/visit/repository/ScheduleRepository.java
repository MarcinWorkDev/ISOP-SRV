package sopi.module.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.visit.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
