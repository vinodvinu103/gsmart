package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;


@Embeddable
public class CompoundPerformanceAppraisal implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long entryTime;

	private String year;

	private String reportingManagerID;

	public void setEntryTime(Long entryTime) {
		this.entryTime = entryTime;
	}

	public Long getEntryTime() {
		return entryTime;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getReportingManagerID() {
		return reportingManagerID;
	}

	public void setReportingManagerID(String reportingManagerID) {
		this.reportingManagerID = reportingManagerID;
	}

}
