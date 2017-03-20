package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TOKEN")
public class Token {

	@Id
	@Column(name="TOKEN_NUMBER")
	String tokenNumber;
	
	@Column(name="SMART_ID")
	String smartId;
	
	@Column(name="ROLE")
	String role;
	
	@Column(name="REPORTINGMANAGER_ID")
	String reportingManagerId;
	
	public String getReportingManagerId() {
		return reportingManagerId;
	}
	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public String getTokenNumber() {
		return tokenNumber;
	}
	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}
	public String getSmartId() {
		return smartId;
	}
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Token [\t tokenNumber=" + tokenNumber + ",\t smartId=" + smartId + ",\t role=" + role + ",\t hierarchy="
				+ hierarchy + "\n\tRole="+role+"\n\tReportingManagerId="+reportingManagerId+"\n\t]";
	}
	
	
	
}
