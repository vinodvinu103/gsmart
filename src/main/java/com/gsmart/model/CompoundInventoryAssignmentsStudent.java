package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundInventoryAssignmentsStudent implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	private String category;
	private String itemType;
	private String entryTime;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	

}
