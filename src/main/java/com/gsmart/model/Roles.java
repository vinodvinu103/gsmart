package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ROLE_MASTER")
public class Roles {
	
	@Id
	@Column(name="ROLE")
	private String role;
	@Column(name="ENTRY_TIME")
	private String entryTime;
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entry_Time) {
		this.entryTime = entry_Time;
	}
	
	

}
