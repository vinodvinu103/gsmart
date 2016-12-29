package com.gsmart.model;

import java.io.Serializable;

public class CompoundModules implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	private String modules;
	private String entryTime;

}
