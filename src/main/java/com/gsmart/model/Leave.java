package com.gsmart.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
@Entity
@Table(name ="APPLY_LEAVE")
@IdClass(com.gsmart.model.CompoundLeave.class)
public class Leave {
	@Id
	@Column(name="SMART_ID")
	private String smartId;
	
	public String getSmartId() {
		return smartId;
	}
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}
	@Column(name="REPORTINGMANAGER_ID")
	private String reportingManagerId;
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column(name="START_DATE")
	private Date startDate;
	@Column(name="END_DATE")
	private Date endDate;
	@Column(name="NUMBER_OF_DAYS")
	private int numberOfDays;
	@Column(name="DESCRIPTION")
	private String description;
	@Column(name="LEAVE_TYPE")
	private String leaveType;
	@Column(name="UPDATED_TIME")
	private String updatedTime;
	@Column(name="IS_ACTIVE")
	private String isActive;
	@Column(name="EXIT_TIME")
	private String exitTime;
	@Column(name="LEAVE_STATUS")
	private String leaveStatus;
	
	
	
	public String getReportingManagerId() {
		return reportingManagerId;
	}
	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getExitTime() {
		return exitTime;
	}
	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	public String getLeaveStatus() {
		return leaveStatus;
	}
	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	@Override
	public String toString() {
		return "Leave [empId=" + smartId + ", reportingManagerId=" + reportingManagerId + ", entryTime=" + entryTime
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", numberOfDays=" + numberOfDays
				+ ", description=" + description + ", leaveType=" + leaveType + ", updatedTime=" + updatedTime
				+ ", isActive=" + isActive + ", exitTime=" + exitTime + ", leaveStatus=" + leaveStatus + "]";
	}
	
	
	
}