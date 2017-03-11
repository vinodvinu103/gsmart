package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompoundSalaryStructure implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="SMART_ID")
	private String smartId;
	
	@Column(name="MONTH")
	private String month;
	
	@Column(name="YEAR")
	private String year;

	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getEmpSmartId() {
		return smartId;
	}

	public void setEmpSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
