package com.gsmart.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * class-name: Holiday.java 
 * Assigning holiday for everyone who enrolled in the school
 * from the principal to the students
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 *
 */
@Entity
@Table(name = "HOLIDAY_MASTER")
@IdClass(com.gsmart.model.CompoundHoliday.class)
public class Holiday {

	/**
	 * Holiday date
	 */
	@Id
	@Column(name="HOLIDAY_DATE")
	private Date holidayDate;
	
	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	/**
	 * description of holiday
	 */
	@Id
	@Column(name="DESCRIPTION")
	private String description;
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	

	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="UPDATED_TIME")
	private String updatedTime;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
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
	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	
	
	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * Gets the HolidayDate
	 * @return HolidayDate, current HolidayDate
	 */


	

	/**
	 * Gets the Description
	 * @return Description, current Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description of Holiday Date
	 * @return updatedTime new updated Time
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Holiday [holidayDate=" + holidayDate + ", description=" + description + ", entryTime=" + entryTime
				+ ", exitTime=" + exitTime + ", updatedTime=" + updatedTime + ", isActive=" + isActive + "]";
	}
	

	/**
	 * Gets the TimeStamp
	 * @return TimeStamp, current TimeStamp
	*/
	
}
