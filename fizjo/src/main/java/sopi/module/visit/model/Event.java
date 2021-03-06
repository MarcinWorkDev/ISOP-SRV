package sopi.module.visit.model;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Event {
	
	private Long profileId;
	private String title;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private DateTime start;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private DateTime end;
	private String className;
	private String description;
	private int eventType;
	private Long scheduleId;
	private boolean past;
	private boolean hasVisit;
	private Long visitId;
	
	public Event(){
		
	}
	
	public Event(
			Long scheduleId, 
			Long profileId, 
			String title, 
			DateTime start, 
			DateTime end, 
			String className, 
			String description, 
			boolean past,
			boolean hasVisit,
			Long visitId) {
		this.profileId = profileId;
		this.title = title;
		this.start = start;
		this.end = end;
		this.className = className;
		this.description = description;
		this.scheduleId = scheduleId;
		this.past = past;
		this.hasVisit = hasVisit;
		this.visitId = visitId;
	}
	
	public Event(String title, DateTime start, DateTime end, String className, String description){
		this.title = title;
		this.start = start;
		this.end = end;
		this.className = className;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public DateTime getStart() {
		return start;
	}
	public void setStart(DateTime start) {
		this.start = start;
	}
	public DateTime getEnd() {
		return end;
	}
	public void setEnd(DateTime end) {
		this.end = end;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public boolean isPast() {
		return past;
	}

	public void setPast(boolean past) {
		this.past = past;
	}

	public boolean isHasVisit() {
		return hasVisit;
	}

	public void setHasVisit(boolean hasVisit) {
		this.hasVisit = hasVisit;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	
}
