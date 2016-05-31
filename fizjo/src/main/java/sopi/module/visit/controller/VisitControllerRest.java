package sopi.module.visit.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<Visit> listvisit(){
		return visitModel.list();
	}
	
	@RequestMapping(value="/setResult", method=RequestMethod.PUT)
	public StatusHelper setResult(@RequestBody VisitResult visitResult){
		
		try {
			visitModel.setResult(visitResult.getVisitId(), visitResult.getStatus(), visitResult.getResult());
			return new StatusHelper(true, "Rekord został zapisany.", visitResult);
		} catch (Exception e){
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), visitResult);
		}
	}
	
	@RequestMapping(value="/cancel/{id}", method=RequestMethod.PUT)
	public StatusHelper cancelVisit(@PathVariable Long id){
		
		try {
			visitModel.cancelVisit(id);
			return new StatusHelper(true, "Wizyta została odwołana.", id);
		} catch (Exception e){
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), id);
		}
	}
	
	@RequestMapping(value="/set", method=RequestMethod.POST)
	public StatusHelper addVisit(@RequestBody VisitNew visit){
		
		if (scheduleModel.getSchedule(visit.getScheduleId()).getHasVisit()){
			return new StatusHelper(false, "Termin jest już zajęty.", visit);
		}
		
		try {
			visitModel.addVisit(visit.getScheduleId(), visit.getProfileId());
			return new StatusHelper(true, "Wizyta została zaplanowana.", visit);
		} catch (Exception e){
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), visit);
		}
	}
}
