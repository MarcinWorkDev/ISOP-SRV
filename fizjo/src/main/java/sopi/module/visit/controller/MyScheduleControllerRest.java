package sopi.module.visit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.person.model.User;
import sopi.module.visit.model.ScheduleModel;

@RestController
@RequestMapping(value="/api/module/myschedule")
public class MyScheduleControllerRest {

	@Autowired ScheduleModel scheduleModel;
	@Autowired AuthUtils auth;
	
	@RequestMapping
	public ResponseEntity<?> getEvents(){
		User user = auth.getLogged();
		return ResponseEntity.ok(scheduleModel.getEventsByPracownik(user.getUserId()));
	}
}
