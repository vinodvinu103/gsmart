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
	private String inTime;
	
	@Column(name="outTime")
	private String outTime;
	
	@Column(name="inDate")
	private long inDate;
	
	@Column(name="isActive")
	private String isActive;
	
	

	
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


	public String getInTime() {
		return inTime;
	}


	public void setInTime(String inTime) {
		this.inTime = inTime;
	}


	public String getOutTime() {
		return outTime;
	}


	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}


	@Override
	public String toString() {
		return "Attendance \n\t [\n\t rfId=" + rfId + ", \n\t smartId=" + smartId + ", \n\t inTime=" + inTime + ", \n\t outTime=" + outTime
				+ ", \n\t inDate=" + inDate + ", \n\t isActive=" + isActive + "]";
	}


	
}
