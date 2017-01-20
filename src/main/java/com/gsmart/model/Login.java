package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "LOGIN")
public class Login implements Serializable {

	@Id
	@Column(name = "SMART_ID")
	private String smartId;

	@Column(name = "PASSWORD")
	private String password;
	@Transient
	@Column(name = "PASSWORD")
	private String confirmPassword;

	@Column(name = "ATTEMPT")
	private Integer attempt;

	@Column(name = "ENTRY_TIME")
	private String entryTime;

	@Column(name = "UPDATED_TIME")
	private String updatedTime;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hid", insertable = false, updatable = false)
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAttempt() {
		return attempt;
	}

	public void setAttempt(Integer attempt) {
		this.attempt = attempt;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
