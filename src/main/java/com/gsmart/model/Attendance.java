package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Attendance")
public class Attendance implements Serializable{
	
	@Id
	@Column(name="RFID")
	private String rfId;
	
	@Column(name="smartId")
	private String smartId;
	
	@Column(name="inTime")
	private long inTime;
	
	@Column(name="outTime")
	private long outTime;
	
	@Column(name="inDate")
	private long inDate;
	
	@Column(name="isActive")
	private String isActive;
	
	@Column(name="STATUS")
	private String status;
	
	

	
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
		return "Attendance \n\t [\n\t rfId=" + rfId + ", \n\t smartId=" + smartId + ", \n\t inTime=" + inTime + ", \n\t outTime=" + outTime
				+ ", \n\t inDate=" + inDate + ", \n\t isActive=" + isActive + "]";
	}


	
}
