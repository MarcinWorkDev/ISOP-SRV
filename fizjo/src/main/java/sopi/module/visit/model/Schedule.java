package sopi.module.visit.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import sopi.module.person.model.Profile;

@Entity(name="schedule")
public class Schedule {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long scheduleId;
	
	@ManyToOne
	@JoinColumn(name="dateId")
	DimDate date;
	
	@ManyToOne
	@JoinColumn(name="timeId")
	DimTime time;
	
	@ManyToOne
	@JoinColumn(name="profileId")
	Profile profile;

	public boolean getPast(){
		
		if (time != null && date != null){
			
			LocalDate nowDate = new LocalDate(new Date());
			LocalTime nowTime = new LocalTime(new Date());
			
			if (date.getDate().isBefore(nowDate)) {
				return true;
			}
			
			if (date.getDate().isAfter(nowDate)) {
				return false;
			}
			
			if (time.getTimeStart().isBefore(nowTime)) {
				return true;
			}
			
			return false;
		} else {
			return false;
		}
	}
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public DimDate getDateId() {
		return date;
	}

	public void setDateId(DimDate date) {
		this.date = date;
	}

	public DimTime getTimeId() {
		return time;
	}

	public void setTimeId(DimTime time) {
		this.time = time;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
