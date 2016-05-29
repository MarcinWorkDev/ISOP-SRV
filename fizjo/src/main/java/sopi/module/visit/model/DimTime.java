package sopi.module.visit.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.LocalTime;

@Entity(name="time")
public class DimTime {

	@Id
	Long timeId;
	
	LocalTime timeStart;
	LocalTime timeEnd;
	
	String timeText;
	
	public Long getTimeId() {
		return timeId;
	}

	public LocalTime getTimeStart() {
		return timeStart;
	}

	public LocalTime getTimeEnd() {
		return timeEnd;
	}

	public String getTimeText() {
		return timeText;
	}
	
	public String getStringStart(){
		return timeStart.toString();
	}
	
	public String getStringEnd(){
		return timeEnd.toString();
	}
	
}
