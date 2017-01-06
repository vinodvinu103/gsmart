package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
@Embeddable
public class CompoundLeave implements Serializable {
private static final long serialVersionUID = 1L;

private String smartId;
public String getSmartId() {
	return smartId;
}
public void setSmartId(String smartId) {
	this.smartId = smartId;
}
//private String reportingManagerId;
private String entryTime;

/*public String getReportingManagerId() {
	return reportingManagerId;
}
public void setReportingManagerId(String reportingManagerId) {
	this.reportingManagerId = reportingManagerId;*/
//}
public String getEntryTime() {
	return entryTime;
}
public void setEntryTime(String entryTime) {
	this.entryTime = entryTime;
}



}
