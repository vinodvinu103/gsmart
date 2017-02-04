package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="notice")
public class Notice 
{
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	@Column(name="SMART_ID")
	private String smartId;
	@Lob @Column(name="MESSAGE")
	private String message;
	@Column(name="EXIT_TIME")
	private String exitTime;
	@Column(name="UPDATE_TIME")
	private String update_time;
	
	@Column(name="ROLE")
	private String role;
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public String getSmartId() {
		return smartId;
	}
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}
	public String getExitTime() {
		return exitTime;
	}
	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getUpdatedId() {
		return updatedId;
	}
	public void setUpdatedId(String updatedId) {
		this.updatedId = updatedId;
	}
	@Column(name="IS_ACTIVE")
	private String isActive;
	@Column(name="TYPE")
	private String type;
	@Column(name="CHILD_FLAG")
	@Transient
	private boolean childFlag;
	@Column(name="UPDATED_ID")
	private String updatedId;
	
	
	public boolean isChildFlag() {
		return childFlag;
	}
	
	public void setChildFlag(boolean childFlag) {
		this.childFlag = childFlag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getSmart_id() {
		return smartId;
	}
	public void setSmart_id(String smart_id) {
		this.smartId = smart_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEntry_time() {
		return entryTime;
	}
	public void setEntry_time(String entry_time) {
		this.entryTime = entry_time;
	}
	public String getExit_time() {
		return exitTime;
	}
	public void setExit_time(String exit_time) {
		this.exitTime = exit_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getIs_active() {
		return isActive;
	}
	public void setIs_active(String is_active) {
		this.isActive = is_active;
	}
	public String getUpdated_id() {
		return updatedId;
	}
	public void setUpdated_id(String updated_id) {
		this.updatedId = updated_id;
	    }
	@Override
	public String toString() {
		return "Notice [entryTime=" + entryTime + ", smartId=" + smartId + ", message=" + message + ", exitTime="
				+ exitTime + ", update_time=" + update_time + ", role=" + role + ", isActive=" + isActive + ", type="
				+ type + ", childFlag=" + childFlag + ", updatedId=" + updatedId + "]";
	}
	
	}
	
	

