package sopi.module.visit.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="time")
public class DimTime {

	@Id
	Long timeId;
	
	@JsonFormat(pattern="HH:mm")
	LocalTime timeStart;
	
	@JsonFormat(pattern="HH:mm")
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
		return timeStart.toString("HH:mm");
	}
	
	public String getStringEnd(){
		return timeEnd.toString("HH:mm");
	}
	
}
