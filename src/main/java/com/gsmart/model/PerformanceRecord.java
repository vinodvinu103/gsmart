package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="PERFORMANCE_RECORD")
@IdClass(com.gsmart.model.CompoundPerformanceAppraisal.class)
public class PerformanceRecord {


	@Id
	@Column(name = "SMART_ID")
	private String smartId;
	
	@Id
	@Column(name = "YEAR")
	private String year;
	
	@Column(name = "RATINGS")
	private String ratings;
	
	public String getRatings() {
		return ratings;
	}

	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

	@Id
	@Column(name = "ENTRY_TIME")
	private Long entryTime;
	
	@Column(name = "REPORTINGMANAGER_ID")
	private String reportingManagerID;
	
	@Column(name = "COMMENTS")
	private String comments;
	  
	@Column(name = "UPDATE_TIME")
	private Long updateTime;
	  
	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "ISACTIVE")
	private String isActive;
	
	
	@Column(name = "ROLE")
	private String role;

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getReportingManagerID() {
		return reportingManagerID;
	}

	public void setReportingManagerID(String reportingManagerID) {
		this.reportingManagerID = reportingManagerID;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	public Long getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Long entryTime) {
		this.entryTime = entryTime;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
