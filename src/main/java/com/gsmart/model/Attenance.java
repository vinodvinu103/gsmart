package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "ATTENANCE")
@IdClass(com.gsmart.model.CompoundAttenance.class)
public class Attenance {
	@Id
	@Column(name = "GSMART_ID")
	private String gsmartId;
	@Id
	@Column(name = "RF_ID")
	private String rfId;
	@Column(name = "IN_TIME")
	private String inTime;
	@Column(name = "OUT_TIME")
	private String outTime;
	public String getRfId() {
		return rfId;
	}
	public void setRfId(String rfId) {
		this.rfId = rfId;
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
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;
	@Column(name = "EXIT_TIME")
	private String exitTime;
	@Column(name = "UPDATE_TIME")
	private String updateTime;
	@Column(name = "IS_ACTIVE")
	private String isActive;
	public String getGsmartId() {
		return gsmartId;
	}
	public void setGsmartId(String gsmartId) {
		this.gsmartId = gsmartId;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public String getExitTime() {
		return exitTime;
	}
	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
}
