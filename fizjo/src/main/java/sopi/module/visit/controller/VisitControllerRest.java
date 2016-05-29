package sopi.module.visit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.visit.model.DimDate;
import sopi.module.visit.model.DimTime;
import sopi.module.visit.model.Event;
import sopi.module.visit.model.Schedule;
import sopi.module.visit.model.ScheduleModel;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/visit")
public class VisitControllerRest {

	@Autowired ScheduleModel scheduleModel;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<Schedule> list() {
		return scheduleModel.getSchedules();
	}
	
	@RequestMapping(value="/schedule/{id}", method=RequestMethod.GET)
	public List<Event> listSchedules(@PathVariable Long id){
		return scheduleModel.getEventsByPracownik(id);
	}
	
	@RequestMapping(value="/schedule/set", method=RequestMethod.POST)
	public StatusHelper setSchedule(@RequestBody Event event){
		try {
			
			scheduleModel.addEvent(event);
			
			return new StatusHelper(true, "Rekord został dodany.", event);
		} catch (Exception e) {
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), event);
		}
	}
	
	@RequestMapping(value="/schedule/delete/{id}", method=RequestMethod.DELETE)
	public StatusHelper deleteSchedule(@PathVariable Long id){
		try {
			scheduleModel.removeSchedule(id);
			return new StatusHelper(true, "Rekord został usunięty.", id);
		} catch (Exception e) {
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), id);
		}
	}
}
