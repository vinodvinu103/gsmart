package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundAttendance implements Serializable {
	private static final long serialVersionUID = 1L;

	
	
	private long inDate;

	

	private String smartId;

	

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public long getInDate() {
		return inDate;
	}

	public void setInDate(long inDate) {
		this.inDate = inDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
