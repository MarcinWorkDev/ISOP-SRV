package sopi.module.visit.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="date")
public class DimDate {

	@Id
	Long dateId;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	LocalDate date;

	public Long getDateId() {
		return dateId;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public int getStatus() {
		LocalDate now = new LocalDate(new Date());
		
		if (date.isAfter(now)) {
			return 1;
		} else if (date.isBefore(now)) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public String getString() {
		return date.toString();
	}
}
