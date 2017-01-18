package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table (name="INVENTORY_ASSIGNMENTS")
@IdClass(com.gsmart.model.InventoryAssignmentsCompoundKey.class)

public class InventoryAssignments implements Serializable
{
		
	@Id
	@Column (name="CATEGORY")
	private String category;
	
	@Id
	@Column (name="ITEM_TYPE")
	private String itemType;

	@Column(name="UPD_SMART_ID")
	private String updSmartId;
	
	
	@Column(name="TEACHER_NAME")
	private String teacherName;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="SECTION")
	private String section;
	
	@Column(name="QUANTITY")
	private int quantity;
	
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column (name="UPDATED_TIME")
	private String updatedTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Id
	@Column(name="SMART_ID")
	private String smartId;
	

	@Transient
	int childFlag;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="SCHOOL", insertable = false, updatable = false),
		@JoinColumn(name="INSTITUTION", insertable = false, updatable = false),
		@JoinColumn(name="ENTRY_TIME", insertable = false, updatable = false)})
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public int getChildFlag() {
		return childFlag;
	}
	public void setChildFlag(int childFlag) {
		this.childFlag = childFlag;
	}
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
	public String getUpdSmartId() {
		return updSmartId;
	}
	public void setUpdSmartId(String updSmartId) {
		this.updSmartId = updSmartId;
	}
	public String getSmartId() {
		return smartId;
	}
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getExitTime() {
		return exitTime;
	}
	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
}
