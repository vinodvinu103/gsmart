package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class CompoundMessageDetails implements Serializable{

 private String smartId;
	 private String entryTime;
	public String getSmartId() {
		return smartId;
	}
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	 
	 
	 
	
	
}
