package sopi.module.visit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.person.model.User;
import sopi.module.visit.model.ScheduleModel;
import sopi.module.visit.model.VisitModel;
import sopi.module.visit.model.VisitNew;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/myvisit")
public class MyVisitControllerRest {

	@Autowired AuthUtils auth;
	@Autowired VisitModel visitModel;
	@Autowired ScheduleModel scheduleModel;
	
	@RequestMapping
	public ResponseEntity<?> getVisit(){
		User user = auth.getLogged();
		
		if (user.getProfile().getType().contains("PACJENT")){
			//return ResponseEntity.noContent().build();
			return ResponseEntity.ok(visitModel.getByPacjent(user.getProfile().getProfileId()));
		}
		return ResponseEntity.ok(visitModel.getByPracownik(user.getProfile().getProfileId()));
	}
	
	@RequestMapping(value="/set", method=RequestMethod.POST)
	public ResponseEntity<?> addVisit(@RequestBody VisitNew visit){
		
		if (scheduleModel.getSchedule(visit.getScheduleId()).getHasVisit()){
			return ResponseEntity.ok(new StatusHelper(false, "Termin jest już zajęty.", visit));
		}
			
		try {
			visitModel.addVisit(visit.getScheduleId(), visit.getProfileId());
			return ResponseEntity.ok(new StatusHelper(true, "Wizyta została zaplanowana.", visit));
		} catch (Exception e){
			return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), visit));
		}
	}
	
	@RequestMapping(value="/future")
	public ResponseEntity<?> getFutureVisit(){
		User user = auth.getLogged();
		
		if (user.getProfile().getType().contains("PACJENT")){
			//return ResponseEntity.noContent().build();
			return ResponseEntity.ok(visitModel.getByPacjentFuture(user.getProfile().getProfileId()));
		}
		return ResponseEntity.ok(visitModel.getByPracownikFuture(user.getProfile().getProfileId()));
	}
	@RequestMapping(value="/past")
	public ResponseEntity<?> getPastVisit(){
		User user = auth.getLogged();
		
		if (user.getProfile().getType().contains("PACJENT")){
			//return ResponseEntity.noContent().build();
			return ResponseEntity.ok(visitModel.getByPacjentPast(user.getProfile().getProfileId()));
		}
		return ResponseEntity.ok(visitModel.getByPracownikPast(user.getProfile().getProfileId()));
	}
}
