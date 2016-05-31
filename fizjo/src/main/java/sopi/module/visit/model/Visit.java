package sopi.module.visit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import sopi.module.person.model.Profile;

@Entity(name="visit")
public class Visit {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long visitId;
	
	@OneToOne
	@JoinColumn(name="scheduleId")
	Schedule schedule;
	
	@ManyToOne
	@JoinColumn(name="profileId")
	Profile profile;
	
	String status;
	
	boolean canceled;
	
	String result;
	
	public String getStatusText(){
		
		if (canceled){
			return "Wizyta odwołana.";
		}
		
		String statusText;
		
		switch(status){
		case "PLA":
			statusText = "Wizyta planowana.";
			break;
		case "ZAP":
			statusText = "Wizyta odbyta.";
			break;
		case "ZAN":
			statusText = "Wizyta nieodbyta.";
			break;
		default:
			statusText = "Status: " + status;
			break;
		}
				
		return statusText;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
