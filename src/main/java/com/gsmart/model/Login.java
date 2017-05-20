package com.gsmart.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "LOGIN")
public class Login  {

	@Id
	@Column(name = "SMART_ID")
//	@Index(name = "smartId")
	private String smartId;

	@Column(name = "PASSWORD")
//	@Index(name = "password")	
	private String password;
	
	@Transient
	@Column(name = "PASSWORD")
	private String newPassword;

	@Transient
	@Column(name = "PASSWORD")
	private String confirmPassword;
	
	@Column(name = "REFERENCESMARTID")
	private String referenceSmartId;	

	public String getReferenceSmartId() {
		return referenceSmartId;
	}

	@Override
	public String toString() {
		return "Login [smartId=" + smartId + ", password=" + password + ", newPassword=" + newPassword
				+ ", confirmPassword=" + confirmPassword + ", referenceSmartId=" + referenceSmartId + ", attempt="
				+ attempt + ", entryTime=" + entryTime + ", updatedTime=" + updatedTime + "]";
	}

	public void setReferenceSmartId(String referenceSmartId) {
		this.referenceSmartId = referenceSmartId;
	}

	@Column(name = "ATTEMPT")
	private Integer attempt;

	@Column(name = "ENTRY_TIME")
	private String entryTime;

	@Column(name = "UPDATED_TIME")
	private String updatedTime;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
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
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
