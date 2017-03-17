package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MODULES")
@IdClass(com.gsmart.model.CompoundModules.class)
public class Modules {
	public String getModules() {
		return modules;
	}
	public void setModules(String modules) {
		this.modules = modules;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public String getSubModules() {
		return subModules;
	}
	public void setSubModules(String subModules) {
		this.subModules = subModules;
	}
	@Id
	@Column(name = "MODULE")
	private String modules;
	
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
	@Column(name = "UPDATE_TIME")
	private String updateTime;
	
	@Column(name = "EXIT_TIME")
	private String exitTime;
	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getExitTime() {
		return exitTime;
	}
	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	@Column(name = "SUB_MODULE")
	private String subModules;
	
	@Override
	public String toString() {
		return "Modules [modules=" + modules + ", entryTime=" + entryTime + ", isActive=" + isActive + ", updateTime="
				+ updateTime + ", exitTime=" + exitTime + ", subModules=" + subModules + ", hierarchy=" + hierarchy
				+ "]";
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	
}
