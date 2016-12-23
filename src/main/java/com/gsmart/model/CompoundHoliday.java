package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
@Embeddable
public class CompoundHoliday implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String holidayDate;
	private String entryTime;
	
	
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public String getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
	}
	
}
