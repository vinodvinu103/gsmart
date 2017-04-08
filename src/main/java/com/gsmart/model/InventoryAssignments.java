package com.gsmart.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name="INVENTORY_ASSIGNMENTS")
@IdClass(com.gsmart.model.InventoryAssignmentsCompoundKey.class)

public class InventoryAssignments
{
		
	@Id
	@Column (name="CATEGORY")
	private String category;
	
	@Id
	@Column (name="ITEM_TYPE")
	private String itemType;
	
	@Column(name="TEACHER_NAME")
	private String teacherName;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="SECTION")
	private String section;
	
	@Column(name="QUANTITY")
	private int quantity;
	
	@Column(name="LEFT_QUANTITY")
	private Integer leftQuantity;

	
	public Integer getLeftQuantity() {
		return leftQuantity;
	}

	public void setLeftQuantity(Integer leftQuantity) {
		this.leftQuantity = leftQuantity;
	}
	@Transient
	private int totalQuantity;
	
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	@Column (name="UPDATED_TIME")
	private String updatedTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	
	@Column(name="SMART_ID")
	private String smartId;
	

	@Transient
	int childFlag;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
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

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	
}
