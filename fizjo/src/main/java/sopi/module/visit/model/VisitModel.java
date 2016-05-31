package sopi.module.visit.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.person.repository.ProfileRepository;
import sopi.module.visit.repository.ScheduleRepository;
import sopi.module.visit.repository.VisitRepository;

@Component
public class VisitModel {

	@Autowired VisitRepository visitRepo;
	@Autowired ScheduleRepository scheduleRepo;
	@Autowired ProfileRepository profileRepo;
	
	public List<Visit> list(){
		return visitRepo.findAll();
	}
	
	public void setResult(Long visitId, String status, String result){
		Visit visit = visitRepo.findOne(visitId);
		
		visit.setStatus(status);
		visit.setResult(result);
		
		visitRepo.save(visit);
	}
	
	public void cancelVisit(Long visitId){
		Visit visit = visitRepo.findOne(visitId);
		
		visit.setCanceled(true);
		
		visitRepo.save(visit);
	}
	
	public void addVisit(Long scheduleId, Long profileId){
		Visit visit = new Visit();
		visit.setCanceled(false);
		visit.setStatus("PLA");
		visit.setSchedule(scheduleRepo.findOne(scheduleId));
		visit.setProfile(profileRepo.findOne(profileId));
		
		visitRepo.save(visit);
	}
	
	public void addVisit(Visit visit){
		visitRepo.save(visit);
	}
	
	public Visit get(Long visitId){
		return visitRepo.findOne(visitId);
	}
}
