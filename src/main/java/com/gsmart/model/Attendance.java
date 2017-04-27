package com.gsmart.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="Attendance")
@IdClass(com.gsmart.model.CompoundAttendance.class)
public class Attendance {
	
	@Column(name="RFID",unique=true)
	private String rfId;
	
	
	@Id
	@Column(name="smartId")
//	@Index(name = "smartId")
	private String smartId;
	
	@Column(name="inTime")
	private long inTime;
	
	@Column(name="outTime")
	private long outTime;
	
	@Id
	@Column(name="inDate")
	private long inDate;
	
	@Column(name="isActive")
//	@Index(name = "isActive")
	private String isActive;
	
	@Column(name="STATUS")
	private String status;
	
	
	@Transient
	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
	
	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name="DEVICE_TOKEN")
	private String finalToken;

	public String getFinalToken() {
		return finalToken;
	}

	public void setFinalToken(String finalToken) {
		this.finalToken = finalToken;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
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
				+ ", inDate=" + inDate + ", isActive=" + isActive + ", status=" + status + ", date=" + date
				+ ", hierarchy=" + hierarchy + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", finalToken=" + finalToken + "]";
	}

	public Hierarchy getHierarchy() {
		return hierarchy;
	}


	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}


	
}
