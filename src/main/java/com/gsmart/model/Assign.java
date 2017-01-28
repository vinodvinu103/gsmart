package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ASSIGN")
@IdClass(com.gsmart.model.CompoundAssign.class)
public class Assign {

	@Id
	@Column(name = "STANDARD")
	private String standard;

	@Column(name = "SECTION")
	private String section;

	@Column(name = "TEACHER_SMART_ID")
	private String teacherSmartId;

	@Column(name = "HOD_SMART_ID")
	private String hodSmartId;

	@Column(name = "PRINCIPAL_SMART_ID")
	private String principalSmartId;

	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;

	@Column(name = "EXIT_TIME")
	private String exitTime;

	@Column(name = "UPDATED_TIME")
	private String updatedTime;

	@Column(name = "IS_ACTIVE")
	private String isActive;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getPrincipalSmartId() {
		return principalSmartId;
	}

	public void setPrincipalSmartId(String principalSmartId) {
		this.principalSmartId = principalSmartId;
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

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getTeacherSmartId() {
		return teacherSmartId;
	}

	public void setTeacherSmartId(String teacherSmartId) {
		this.teacherSmartId = teacherSmartId;
	}

	public String getHodSmartId() {
		return hodSmartId;
	}

	public void setHodSmartId(String hodSmartId) {
		this.hodSmartId = hodSmartId;
	}

}
