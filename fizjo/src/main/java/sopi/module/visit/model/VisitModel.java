package sopi.module.visit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.email.model.EmailModel;
import sopi.module.person.repository.ProfileRepository;
import sopi.module.visit.repository.ScheduleRepository;
import sopi.module.visit.repository.VisitRepository;

@Component
public class VisitModel {

	@Autowired VisitRepository visitRepo;
	@Autowired ScheduleRepository scheduleRepo;
	@Autowired ProfileRepository profileRepo;
	@Autowired EmailModel emailModel;
	
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
		
		String emailTo = visit.getProfile().getEmail();
		String emailSubject = "Twoja wizyta została anulowana.";		
		String emailTemplateName = "mail/cancelVisit";
		Map<String, Object> emailTemplateVars = new HashMap<String, Object>();
		emailTemplateVars.put("pacjent", visit.getProfile());
		emailTemplateVars.put("dataWizyty", visit.getSchedule().getDateId().getString());
		emailTemplateVars.put("godzinaWizyty", visit.getSchedule().getTimeId().getTimeText());
		emailTemplateVars.put("specjalista", 
				visit.getSchedule().getProfile().getNazwisko() + " " + visit.getSchedule().getProfile().getImie());
		
		emailModel.sendEmail(emailTo, emailSubject, emailTemplateName, emailTemplateVars, true);
	}
	
	public void addVisit(Long scheduleId, Long profileId){
		Visit visit = new Visit();
		visit.setCanceled(false);
		visit.setStatus("PLA");
		visit.setSchedule(scheduleRepo.findOne(scheduleId));
		visit.setProfile(profileRepo.findOne(profileId));
		
		visitRepo.save(visit);
		
		String emailTo = visit.getProfile().getEmail();
		String emailSubject = "Termin wizyty został zarezerwowany.";		
		String emailTemplateName = "mail/newVisit";
		Map<String, Object> emailTemplateVars = new HashMap<String, Object>();
		emailTemplateVars.put("pacjent", visit.getProfile());
		emailTemplateVars.put("dataWizyty", visit.getSchedule().getDateId().getString());
		emailTemplateVars.put("godzinaWizyty", visit.getSchedule().getTimeId().getTimeText());
		emailTemplateVars.put("specjalista", 
				visit.getSchedule().getProfile().getNazwisko() + " " + visit.getSchedule().getProfile().getImie());
		
		emailModel.sendEmail(emailTo, emailSubject, emailTemplateName, emailTemplateVars, true);
	}
	
	public void addVisit(Visit visit){
		visitRepo.save(visit);
		
		String emailTo = visit.getProfile().getEmail();
		String emailSubject = "Termin wizyty został zarezerwowany.";		
		String emailTemplateName = "mail/newVisit";
		Map<String, Object> emailTemplateVars = new HashMap<String, Object>();
		emailTemplateVars.put("pacjent", visit.getProfile());
		emailTemplateVars.put("dataWizyty", visit.getSchedule().getDateId().getString());
		emailTemplateVars.put("godzinaWizyty", visit.getSchedule().getTimeId().getTimeText());
		emailTemplateVars.put("specjalista", 
				visit.getSchedule().getProfile().getNazwisko() + " " + visit.getSchedule().getProfile().getImie());
		
		emailModel.sendEmail(emailTo, emailSubject, emailTemplateName, emailTemplateVars, true);
	}
	
	public Visit get(Long visitId){
		return visitRepo.findOne(visitId);
	}
	
	public List<Visit> getByPacjent(Long profileId){
		return visitRepo.findAllByProfileProfileId(profileId);
	}
	public List<Visit> getByPracownik(Long profileId){
		return visitRepo.findAllByScheduleProfileProfileId(profileId);
	}
	public List<Visit> getByPacjentFuture(Long profileId){
		List<Visit> visits = visitRepo.findAllByProfileProfileIdOrderByScheduleDateDateId(profileId);
		List<Visit> list = new ArrayList<>();
		
		for (Visit visit : visits){
			if (!visit.getSchedule().getPast() && !visit.isCanceled()){
				list.add(visit);
			}
		}
		
		return list;
	}
	public List<Visit> getByPracownikFuture(Long profileId){
		List<Visit> visits = visitRepo.findAllByScheduleProfileProfileIdOrderByScheduleDateDateId(profileId);
		List<Visit> list = new ArrayList<>();
		
		for (Visit visit : visits){
			if (!visit.getSchedule().getPast() && !visit.isCanceled()){
				list.add(visit);
			}
		}
		
		return list;
	}
	public List<Visit> getByPacjentPast(Long profileId){
		List<Visit> visits = visitRepo.findAllByProfileProfileIdOrderByScheduleDateDateIdDesc(profileId);
		List<Visit> list = new ArrayList<>();
		
		for (Visit visit : visits){
			if (visit.getSchedule().getPast() && !visit.isCanceled()){
				list.add(visit);
			}
		}
		
		return list;
	}
	public List<Visit> getByPracownikPast(Long profileId){
		List<Visit> visits = visitRepo.findAllByScheduleProfileProfileIdOrderByScheduleDateDateIdDesc(profileId);
		List<Visit> list = new ArrayList<>();
		
		for (Visit visit : visits){
			if (visit.getSchedule().getPast() && !visit.isCanceled()){
				list.add(visit);
			}
		}
		
		return list;
	}
	
}
