package sopi.module.visit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.visit.model.Event;
import sopi.module.visit.model.Schedule;
import sopi.module.visit.model.ScheduleModel;

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
}
