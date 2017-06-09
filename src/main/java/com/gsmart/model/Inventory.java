package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * class-name: Inventory.java Assigning inventory for everyone who enrolled in
 * the school from the principal to the students
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 *
 */
@Entity
@Table(name = "INVENTORY")
@IdClass(com.gsmart.model.CompoundInventory.class)
public class Inventory {

	@Id
	@Column(name="CATEGORY")
//	@Index(name = "category")
	private String category;

    @Id
	@Column(name="ITEM_Type")
//	@Index(name = "itemType")
	private String itemType;

	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column(name="QUANTITY")
	private Integer quantity;
	
	@Column(name="LEFT_QUANTITY")
	private Integer leftQuantity;
	
	@Column(name="EXIT_TIME")
	private String exitTime;

	/**
	 * Its a time when the inventory instance is editing the item
	 */
	@Column(name="UPDATE_TIME")
	private String updateTime;
	@Column(name="ISACTIVE")
	private String isActive;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
//	@Index(name = "hierarchy")
	private Hierarchy hierarchy;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getLeftQuantity() {
		return leftQuantity;
	}

	public void setLeftQuantity(Integer leftQuantity) {
		this.leftQuantity = leftQuantity;
	}

	/**
	 * Its a time when the inventory instance is exit
	 */
	

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}



	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Inventory [category=" + category + ", itemType=" + itemType + ", entryTime=" + entryTime + ", quantity="
				+ quantity + ", leftQuantity=" + leftQuantity + ", isActive=" + isActive + ", hierarchy=" + hierarchy
				+ "]";
	}

	

	


	

}
