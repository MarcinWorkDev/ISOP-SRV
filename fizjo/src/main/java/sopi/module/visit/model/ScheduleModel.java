package sopi.module.visit.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.visit.repository.ScheduleRepository;

@Component
public class ScheduleModel {

	@Autowired ScheduleRepository scheduleRepo;
	
	public List<Schedule> getSchedules(){
		return scheduleRepo.findAll();
	}
}
