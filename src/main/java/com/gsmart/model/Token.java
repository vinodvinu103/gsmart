package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
		
		return "\nToken [\n\tTokenNumber="+tokenNumber+"\n\tSmartID="+smartId+"\n\tRole="+role+" ]";
	}
	
}