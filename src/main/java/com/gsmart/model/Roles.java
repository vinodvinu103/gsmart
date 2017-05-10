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
	private String entry_Time;
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEntry_Time() {
		return entry_Time;
	}
	public void setEntry_Time(String entry_Time) {
		this.entry_Time = entry_Time;
	}
	
	

}
