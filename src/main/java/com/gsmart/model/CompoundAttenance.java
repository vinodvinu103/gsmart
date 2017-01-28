package com.gsmart.model;
import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class CompoundAttenance implements Serializable {
	private static final long serialVersionUID = 1L;
	private String gsmartId;
	private String entryTime;
	private String rfId;
	public String getRfId() {
		return rfId;
	}
	public void setRfId(String rfId) {
		this.rfId = rfId;
	}
	public String getGsmartId() {
		return gsmartId;
	}
	public void setGsmartId(String gsmartId) {
		this.gsmartId = gsmartId;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

}
