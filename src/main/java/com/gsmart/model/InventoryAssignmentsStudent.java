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
@Table(name="INVENTORY_ASSIGNMENTS_STUDENT")
@IdClass(com.gsmart.model.CompoundInventoryAssignmentsStudent.class)
public class InventoryAssignmentsStudent {
	
	@Id
	@Column (name="CATEGORY")
	private String category;
	
	@Id
	@Column (name="ITEM_TYPE")
	private String itemType;
	
	@Column(name="STUDENT_NAME")
	private String studentName;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="SECTION")
	private String section;
	
	@Column(name="QUANTITY")
	private int quantity;
	
	@Transient
	private int totalQuantity;
	
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column (name="UPDATED_TIME")
	private String updatedTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="SMART_ID")
	private String smartId;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}


	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
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

	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
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


	public int getTotalQuantity() {
		return totalQuantity;
	}


	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
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


	public String getSmartId() {
		return smartId;
	}


	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}


	@Override
	public String toString() {
		return "InventoryAssignmentsStudent [category=" + category + ", itemType=" + itemType + ", studentName="
				+ studentName + ", standard=" + standard + ", section=" + section + ", quantity=" + quantity
				+ ", totalQuantity=" + totalQuantity + ", entryTime=" + entryTime + ", updatedTime=" + updatedTime
				+ ", exitTime=" + exitTime + ", isActive=" + isActive + ", smartId=" + smartId + "]";
	}
	
}
