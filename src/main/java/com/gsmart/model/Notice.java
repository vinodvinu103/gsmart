package com.gsmart.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "notice")
public class Notice {
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;
	@Column(name = "SMART_ID")
//	@Index(name = "smartId")
	private String smartId;
	@Lob
	@Column(name = "MESSAGE")
	private String message;
	@Column(name = "EXIT_TIME")
	private String exitTime;
	@Column(name = "UPDATE_TIME")
	private String updateTime;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
     
	@Column(name = "MESSAGE_Sub")
	private String messagesubject;
	
	@Lob
	@Column(name = "Image")
//	@Index(name = "image")
	private byte[] image;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "CHILD_FLAG")
	@Transient
	private boolean childFlag;
	@Column(name = "UPDATED_ID")
	private String updatedId;
	
	@Column(name= "Format")
	private String format;

	@Column(name = "ROLE")
	private String role;

	
	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	

	
	
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	

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

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUpdate_time() {
		return updateTime;
	}

	public void setUpdate_time(String update_time) {
		this.updateTime = update_time;
	}
	
	

	public String getMessagesubject() {
		return messagesubject;
	}

	public void setMessagesubject(String messagesubject) {
		this.messagesubject = messagesubject;
	}

	@Override
	public String toString() {
		return "Notice [entryTime=" + entryTime + ", smartId=" + smartId + ", message=" + message + ", exitTime="
				+ exitTime  + ", hierarchy=" + hierarchy + ", messagesubject="
				+ messagesubject + ", image=" + Arrays.toString(image) + ", format=" + format + ", role=" + role
				+ ", isActive=" + isActive + ", type=" + type + ", childFlag=" + childFlag + ", updatedId=" + updatedId
				+ "]";
	}
	

	}


