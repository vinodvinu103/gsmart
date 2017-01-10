package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundReportCard implements Serializable{

	private static final long serialVersionUID = 1L;
	
	 private String smartId;
	 
	 private String entryTime;
	 
	 private String subject;
	 

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	

	 
	
	 
}
