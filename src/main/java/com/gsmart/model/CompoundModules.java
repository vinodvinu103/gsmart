package com.gsmart.model;

import java.io.Serializable;

public class CompoundModules implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String moduleName;
	private String entryTime;
	
	public String getModules() {
		return moduleName;
	}
	public void setModules(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	
	

}
