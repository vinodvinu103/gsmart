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
	
	@Column(name = "SUB_MODULE")
	private String subModules;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Override
	public String toString() {
		return "Modules [modules=" + modules + ", entryTime=" + entryTime + ", subModules=" + subModules + "]";
	}

}
