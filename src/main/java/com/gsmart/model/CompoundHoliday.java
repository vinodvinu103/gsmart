package com.gsmart.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
@Embeddable
public class CompoundHoliday implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date holidayDate;
	private String entryTime;
	
	
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public Date getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}
	
}
