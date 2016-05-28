package sopi.module.visit.model;

public class Event {
	
	private String title;
	private String start;
	private String end;
	private String className;
	private String description;
	private int eventType;
	private Long wizytaId;
	private Long pacjentId;
	private Long grafikId;
	
	public Event(String title, String start, String end, String className, String description, int eventType, Long pacjentId, Long wizytaId, Long grafikId) {
		this.title = title;
		this.start = start;
		this.end = end;
		this.className = className;
		this.description = description;
		this.eventType = eventType;
		this.wizytaId = wizytaId;
		this.pacjentId = pacjentId;
		this.grafikId = grafikId;
	}
	
	public Event(String title, String start, String end, String className, String description){
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
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
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

	public Long getWizytaId() {
		return wizytaId;
	}

	public void setWizytaId(Long wizytaId) {
		this.wizytaId = wizytaId;
	}

	public Long getPacjentId() {
		return pacjentId;
	}

	public void setPacjentId(Long pacjentId) {
		this.pacjentId = pacjentId;
	}

	public Long getGrafikId() {
		return grafikId;
	}

	public void setGrafikId(Long grafikId) {
		this.grafikId = grafikId;
	}
	
}
