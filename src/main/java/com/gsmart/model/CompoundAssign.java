package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundAssign implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	private String standard;
	
	
	
	
	private String entryTime;
	
	
	
	
	private String isActive;

	
	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}


	@Override
	public String toString() {
		return "CompoundAssign [standard=" + standard + ", entryTime=" + entryTime + ", isActive=" + isActive + "]";
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}


	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
	
	

}
