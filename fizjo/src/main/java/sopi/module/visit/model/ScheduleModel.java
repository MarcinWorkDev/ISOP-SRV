package sopi.module.visit.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.person.repository.ProfileRepository;
import sopi.module.visit.repository.DimDateRepository;
import sopi.module.visit.repository.DimTimeRepository;
import sopi.module.visit.repository.ScheduleRepository;

@Component
public class ScheduleModel {

	@Autowired ScheduleRepository scheduleRepo;
	@Autowired DimTimeRepository dimTimeRepo;
	@Autowired DimDateRepository dimDateRepo;
	@Autowired ProfileRepository profileRepo;
	
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
				title = "Zakończony";
				klasa = "schedule-unavail";
				description = "Dyżur zakończony.";
			} else {
				title = "Planowany";
				klasa = "schedule-avail";
				description = "Dyżur planowany.";
			}
			
			DateTime start = new DateTime(schedule.getDateId().getString() + 'T' + schedule.getTimeId().getStringStart());
			DateTime end = new DateTime(schedule.getDateId().getString() + 'T' + schedule.getTimeId().getStringEnd());
			
			events.add(new Event(
					schedule.getScheduleId(), 
					profileId, 
					title, 
					start, 
					end, 
					klasa, 
					description, 
					status,
					schedule.getHasVisit(),
					schedule.getVisitId()
					));
		}
		
		return events;
	}
	
	public List<Event> getEventsAllFuture(){
		List<Schedule> schedules = scheduleRepo.findAll();
		List<Event> events = new ArrayList<>();
		
		for (Schedule schedule : schedules){
					
			if (schedule.getPast()) {
				continue;
			}
			
			String title = "";
			String klasa = "";
			String description = "";
			
			boolean status = schedule.getPast();
			
			if (status){
				title = "Niedostępny";
				klasa = "schedule-unavail";
				description = "Termin jest niedostępny.";
			} else if(schedule.getHasVisit()) {
				title = "Termin zajęty.";
				klasa = "schedule-unavail-b";
				description = "Termin jest zarezerwowany.";
			} else {
				title = "Termin dostępny.";
				klasa = "schedule-avail";
				description = "Brak rezerwacji terminu.";
			}
			
			DateTime start = new DateTime(schedule.getDateId().getString() + 'T' + schedule.getTimeId().getStringStart());
			DateTime end = new DateTime(schedule.getDateId().getString() + 'T' + schedule.getTimeId().getStringEnd());
			
			events.add(new Event(
					schedule.getScheduleId(), 
					schedule.getProfile().getProfileId(), 
					title, 
					start, 
					end, 
					klasa, 
					description, 
					status,
					schedule.getHasVisit(),
					schedule.getVisitId()
					));
		}
		
		return events;
	}
	
	public void addEvent(Event event){
		DimDate date = dimDateRepo.findOne(Long.parseLong(event.getStart().toString("yyyyMMdd")));
		DimTime time = dimTimeRepo.findOne(Long.parseLong("9" + event.getStart().toString("HH") + event.getEnd().toString("HH")));
		
		Schedule schedule = new Schedule();
		schedule.setDateId(date);
		schedule.setTimeId(time);
		schedule.setProfile(profileRepo.findOne(event.getProfileId()));
		
		scheduleRepo.save(schedule);
	}
	
	public void removeSchedule(Long id){
		scheduleRepo.delete(id);
	}
}
