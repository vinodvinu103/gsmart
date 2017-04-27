package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@SuppressWarnings("deprecation")
@Entity
@Table(name="Attendance")
@IdClass(com.gsmart.model.CompoundAttendance.class)
public class Attendance {
	
	@Id
	@Column(name="RFID")
	private String rfId;
	
	@Column(name="smartId")
	@Index(name = "smartId")
	private String smartId;
	
	@Column(name="inTime")
	private long inTime;
	
	@Column(name="outTime")
	private long outTime;
	
	@Id
	@Column(name="inDate")
	private long inDate;
	
	@Column(name="isActive")
	@Index(name = "isActive")
	private String isActive;
	
	@Column(name="STATUS")
	private String status;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
	
	@Column(name="     role")
	private String role;
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public long getInDate() {
		return inDate;
	}


	public void setInDate(long inDate) {
		this.inDate = inDate;
	}


	public String getIsActive() {
		return isActive;
	}


	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public String getRfId() {
		return rfId;
	}


	public void setRfId(String rfId) {
		this.rfId = rfId;
	}


	public String getSmartId() {
		return smartId;
	}


	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}


	public long getInTime() {
		return inTime;
	}


	public void setInTime(long inTime) {
		this.inTime = inTime;
	}


	public long getOutTime() {
		return outTime;
	}


	public void setOutTime(long outTime) {
		this.outTime = outTime;
	}


	


	@Override
	public String toString() {
		return "Attendance [rfId=" + rfId + ", smartId=" + smartId + ", inTime=" + inTime + ", outTime=" + outTime
				+ ", inDate=" + inDate + ", isActive=" + isActive + ", status=" + status + ", hierarchy=" + hierarchy
				+ ", role=" + role + "]";
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
