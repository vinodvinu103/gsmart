package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "PERFORMANCE")
@IdClass(com.gsmart.model.CompoundPerformanceAppraisal.class)
public class PerformanceAppraisal {
	@Id
	@Column(name = "REPORTINGMANAGER_ID")
	private String reportingManagerID;

	@Id
	@Column(name = "ACADAMIC_YEAR")
	private String year;

	@Id
	@Column(name = "ENTRY_TIME")
	private Long entryTime;

	@Column(name = "SCHOOL_PERFARMANCE", columnDefinition = "text")
	private String schoolPerformance;

	@Column(name = "CHARITY", columnDefinition = "text")
	private String charity;

	@Column(name = "SPORTS", columnDefinition = "text")
	private String sports;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "EXIT_TIME")
	private Long exitTime;

	@Column(name = "UPDATE_TIME")
	private Long updateTime;

	public String getReportingManagerID() {
		return reportingManagerID;
	}

	public void setReportingManagerID(String reportingManagerID) {
		this.reportingManagerID = reportingManagerID;
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

	public void setEntryTime(Long entryTime) {
		this.entryTime = entryTime;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Long getExitTime() {
		return exitTime;
	}

	public void setExitTime(Long exitTime) {
		this.exitTime = exitTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "PerformanceAppraisal [reportingManagerID=" + reportingManagerID + ", year=" + year + ", entryTime="
				+ entryTime + ", schoolPerformance=" + schoolPerformance + ", charity=" + charity + ", sports=" + sports
				+ ", isActive=" + isActive + ", exitTime=" + exitTime + ", updateTime=" + updateTime + "]";
	}

}
