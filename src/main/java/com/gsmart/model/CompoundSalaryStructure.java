package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompoundSalaryStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="EMP_SMART_ID")
	private Integer empSmartId;
	
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

	public Integer getEmpSmartId() {
		return empSmartId;
	}

	public void setEmpSmartId(Integer empSmartId) {
		this.empSmartId = empSmartId;
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
