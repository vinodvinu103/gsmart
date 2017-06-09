package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class CompoundFeeMaster implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name ="STANDARD")
	private String standard;
	
	
	@Column(name ="ENTRY_TIME")
	private String entryTime;
	
	
	

	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	
}
