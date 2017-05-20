package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="PERFORMANCE_RECORD")
@IdClass(com.gsmart.model.CompoundPerformanceRecord.class)
public class PerformanceRecord {


	@Id
	@Column(name = "SMART_ID")
	private String smartId;
	
	@Id
	@Column(name = "YEAR")
	private String year;
	
    
	
	@Column(name = "REPORTINGMANAGER_ID")
	private String reportingManagerID;
	
	
	@Column(name = "RATINGS")
	private String ratings;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	

	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	@Id
	@Column(name = "ENTRY_TIME")
	private Long entryTime;
	

	
	@Column(name = "COMMENTS")
	private String comments;
	  
	@Column(name = "UPDATE_TIME")
	private Long updateTime;
	  


	@Column(name = "ISACTIVE")
	private String isActive;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	
	
	@Override
	public String toString() {
		return "PerformanceRecord [smartId=" + smartId + ", year=" + year + ", reportingManagerID=" + reportingManagerID
				+ ", ratings=" + ratings + ", entryTime=" + entryTime + ", comments=" + comments + ", updateTime="
				+ updateTime + ", isActive=" + isActive + ", hierarchy=" + hierarchy + ", role=" + role + "]";
	}



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



	public String getRatings() {
		return ratings;
	}



	public void setRatings(String ratings) {
		this.ratings = ratings;
	}



	public Long getEntryTime() {
		return entryTime;
	}



	public void setEntryTime(Long entryTime) {
		this.entryTime = entryTime;
	}



	public String getComments() {
		return comments;
	}



	public void setComments(String comments) {
		this.comments = comments;
	}



	public Long getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}



	public String getIsActive() {
		return isActive;
	}



	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}



	public Hierarchy getHierarchy() {
		return hierarchy;
	}



	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}

	
	
}
