package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
@Embeddable
public class CompoundWeekDays implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String weekDay;
	private String entryTime;
	
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	
	
	
	
	
	
}	
	