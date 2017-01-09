package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundLeaveDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String smartId;
	
	
	public String getSmartId() {
		return smartId;
	}


	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}


	public int getAppliedLeaves() {
		return appliedLeaves;
	}


	public void setAppliedLeaves(int appliedLeaves) {
		this.appliedLeaves = appliedLeaves;
	}


	private int appliedLeaves;
	
	
	

}
