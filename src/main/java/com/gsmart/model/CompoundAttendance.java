package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundAttendance implements Serializable {
	private static final long serialVersionUID = 1L;

	private String rfId;
	
	private long inDate;

	public String getRfId() {
		return rfId;
	}

	@Override
	public String toString() {
		return "CompoundAttendance [rfId=" + rfId + ", inDate=" + inDate + "]";
	}

	public void setRfId(String rfId) {
		this.rfId = rfId;
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
