package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "PERFARMANCE")
@IdClass(com.gsmart.model.CompoundPerformanceAppraisal.class)
public class PerformanceAppraisal {
	@Id
	@Column(name = "SMART_ID")
	private String smartId;

	@Id
	@Column(name = "ACADAMIC_YEAR")
	private String year;

	@Id
	@Column(name = "ENTRY_TIME")
	private Long entryTime;

	@Column(name = "SCHOOL_PERFARMANCE")
	private String schoolPerformance;

	@Column(name = "CHARITY")
	private String charity;

	@Column(name = "SPORTS")
	private String sports;

	@Column(name = "ISACTIVE")
	private String isActive;
	
	@Column(name = "EXIT_TIME")
	private String exitTime;
	
	@Column(name = "UPDATE_TIME")
	private Long updateTime;

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getSchoolPerformance() {
		return schoolPerformance;
	}

	public void setSchoolPerformance(String schoolPerformance) {
		this.schoolPerformance = schoolPerformance;
	}

	public String getCharity() {
		return charity;
	}

	public void setCharity(String charity) {
		this.charity = charity;
	}

	public String getSports() {
		return sports;
	}

	public void setSports(String sports) {
		this.sports = sports;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Long currentEpoch) {
		this.entryTime = entryTime;

	}

	public void setExitTime(Long currentEpoch) {
		this.exitTime = exitTime;

	}

}
