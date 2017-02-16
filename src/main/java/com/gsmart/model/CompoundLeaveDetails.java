package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundLeaveDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String smartId;
	
	private String leaveType;
	
	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	
}
