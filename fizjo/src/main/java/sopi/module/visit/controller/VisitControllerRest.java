package sopi.module.visit.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.visit.model.ScheduleModel;
import sopi.module.visit.model.Visit;
import sopi.module.visit.model.VisitModel;
import sopi.module.visit.model.VisitNew;
import sopi.module.visit.model.VisitResult;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/visit")
public class VisitControllerRest {

	@Autowired VisitModel visitModel;
	@Autowired ScheduleModel scheduleModel;
	@Autowired AuthUtils auth;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public ResponseEntity<?> listvisit(){
		if (auth.checkRoles("ADMIN,VISIT"))
		{
			return ResponseEntity.ok(visitModel.list());
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
	@RequestMapping(value="/setResult", method=RequestMethod.PUT)
	public ResponseEntity<?> setResult(@RequestBody VisitResult visitResult){
		if (auth.checkRoles("ADMIN,VISIT,T_PRACOWNIK"))
		{
			try {
				visitModel.setResult(visitResult.getVisitId(), visitResult.getStatus(), visitResult.getResult());
				return ResponseEntity.ok(new StatusHelper(true, "Rekord został zapisany.", visitResult));
			} catch (Exception e){
				return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), visitResult));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
	@RequestMapping(value="/cancel/{id}", method=RequestMethod.PUT)
	public ResponseEntity<?> cancelVisit(@PathVariable Long id){
		
		if (auth.checkRoles("ADMIN,VISIT,T_PRACOWNIK,T_PACJENT"))
		{
			Visit visit = visitModel.get(id);
			
			if (auth.checkRoles("T_PRACOWNIK") && auth.checkRoles("ADMIN,VISIT")){
				if (auth.getLogged().getUserId() != visit.getSchedule().getProfile().getProfileId()){
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
				}
			}
			
			if (auth.checkRoles("T_PACJENT") && auth.checkRoles("ADMIN,VISIT")){
				if (auth.getLogged().getUserId() != visit.getProfile().getProfileId()){
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
				}
			}
			
			try {
				visitModel.cancelVisit(id);
				return ResponseEntity.ok(new StatusHelper(true, "Wizyta została odwołana.", id));
			} catch (Exception e){
				return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), id));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
	@RequestMapping(value="/set", method=RequestMethod.POST)
	public ResponseEntity<?> addVisit(@RequestBody VisitNew visit){
		
		if (auth.checkRoles("ADMIN,VISIT"))
		{
			if (scheduleModel.getSchedule(visit.getScheduleId()).getHasVisit()){
				return ResponseEntity.ok(new StatusHelper(false, "Termin jest już zajęty.", visit));
			}
			
			try {
				visitModel.addVisit(visit.getScheduleId(), visit.getProfileId());
				return ResponseEntity.ok(new StatusHelper(true, "Wizyta została zaplanowana.", visit));
			} catch (Exception e){
				return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), visit));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
}
