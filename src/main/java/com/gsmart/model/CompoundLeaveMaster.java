package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundLeaveMaster implements Serializable{
	private static final long serialVersionUID = 1L;
	private String leaveType;
	private int daysAllow;
	private String entryTime;
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public int getDaysAllow() {
		return daysAllow;
	}
	public void setDaysAllow(int daysAllow) {
		this.daysAllow = daysAllow;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

}
