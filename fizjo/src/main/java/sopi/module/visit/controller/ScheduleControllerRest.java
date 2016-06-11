package sopi.module.visit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.visit.model.Event;
import sopi.module.visit.model.Schedule;
import sopi.module.visit.model.ScheduleModel;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/schedule")
public class ScheduleControllerRest {

	@Autowired ScheduleModel scheduleModel;
	@Autowired AuthUtils auth;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<Schedule> list() {
		return scheduleModel.getSchedules();
	}
	
	@RequestMapping(value="/event/get/pracownik/{id}", method=RequestMethod.GET)
	public List<Event> listSchedules(@PathVariable Long id){
		return scheduleModel.getEventsByPracownik(id);
	}
	
	@RequestMapping(value="/event/get", method=RequestMethod.GET)
	public List<Event> listSchedulesAll(){
		return scheduleModel.getEventsAllFuture();
	}
	
	@RequestMapping(value="/event/set", method=RequestMethod.POST)
	public ResponseEntity<?> setSchedule(@RequestBody Event event){
		if (auth.checkRoles("ADMIN,SCHEDULE"))
		{
			try {
				scheduleModel.addEvent(event);
				return ResponseEntity.ok(new StatusHelper(true, "Rekord został dodany.", event));
			} catch (Exception e) {
				return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), event));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteSchedule(@PathVariable Long id){
		if (auth.checkRoles("ADMIN,SCHEDULE"))
		{
			try {
				scheduleModel.removeSchedule(id);
				return ResponseEntity.ok(new StatusHelper(true, "Rekord został usunięty.", id));
			} catch (Exception e) {
				return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), id));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
}
