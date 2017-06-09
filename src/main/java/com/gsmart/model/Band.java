package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


/**
 * class-name: Band.java 
 * Assigning band for everyone who enrolled in the school
 * from the principal to the students
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-02-23
 *
 */
@Entity
@Table(name = "BAND_MASTER")
@IdClass(com.gsmart.model.CompoundBand.class)
public class Band {

	/**
	 * Band Id of a person
	 */
	@Id
	@Column(name = "BAND_ID")
//	@Index(name= "bandId")
	private int bandId;
	/**
	 * Designation of the particular person 
	 */
	@Id
	@Column(name = "DESIGNATION")
	private String designation;
	/**
	 * Role of the particular person 
	 */
	@Id
	@Column(name = "ROLE")
//	@Index(name = "role")
	private String role;
	/**
	 * timeStamp is the Time when the band entity is added
	 */
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="UPDATED_TIME")
        private String updatedTime;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
    
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the bandId of the person
	 * 
	 * @return this person's(current bandId) BandId
	 */
	
	public int getBandId() {
		return bandId;
	}

	/**
	 * Changes the BandId of the person
	 * 
	 * @param bandId
	 *            new Band Id of the person
	 */
	public void setBandId(int bandId) {
		this.bandId = bandId;
	}

	/**
	 * Gets the designation of the person
	 * 
	 * @return this person's or current designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * Changes the Designation of the person
	 * 
	 * @param designation
	 *            new designation of the person
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * Gets the role of the person
	 * 
	 * @return role current role of the person
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Changes the role of the person
	 * 
	 * @return role new role of the person
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * Gets the entry time
	 * 
	 * @return timeStamp current timeStamp
	 */

	@Override
	public String toString() {
		return "Band [\n\t bandId=" + bandId + ", \n\t designation=" + designation + ", \n\t role=" + role + ",\n\t entryTime=" + entryTime
				+ ", \n\t exitTime=" + exitTime + ", \n\t updatedTime=" + updatedTime + ",\n\t isActive=" + isActive + " \n]";
	}

}
