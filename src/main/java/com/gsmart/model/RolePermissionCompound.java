package com.gsmart.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class RolePermissionCompound implements Serializable {

	private static final long serialVersionUID = 1L;
	private String role;
	private String moduleName;
	private String entryTime;

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	
}
