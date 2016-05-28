package sopi.module.visit.model;

import java.util.ArrayList;
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
	
	public List<Event> getEventsByPracownik(Long profileId){
		List<Schedule> schedules = scheduleRepo.findAllByProfileProfileId(profileId);
		List<Event> events = new ArrayList<>();
		
		for (Schedule schedule : schedules){
					
			String title = "";
			String klasa = "";
			String description = "";
			
			boolean status = schedule.getPast();
			
			if (status){
				title = "Zrealizowany";
				klasa = "schedule-unavail";
				description = "Dyżur odbyty.";
			} else {
				title = "Planowany";
				klasa = "schedule-avail";
				description = "Dyżur planowany.";
			}
			
			String start = schedule.getDateId().getString() + 'T' + schedule.getTimeId().getStringStart();
			String end = schedule.getDateId().getString() + 'T' + schedule.getTimeId().getStringEnd();
			
			events.add(new Event(title, start, end, klasa, description));
		}
		
		return events;
	}
}
