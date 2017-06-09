/*
* class name: Hierarchy.java
 *
 * Copyright (c) 2008-2009 Gowdanar Technologies Pvt. Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Gowdanar
 * Technologies Pvt. Ltd.("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Gowdanar Technologies.
 *
 * GOWDANAR TECHNOLOGIES MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GOWDANAR TECHNOLOGIES SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING
 */
package com.gsmart.model;

import javax.persistence.*;

/**
 * class-name: Hierarchy.java
 * Assigning hierarchy for everyone 
 * from the institution to the section
 *   
 * @author 
 * @version 1.0
 * @since 2016-02-23  
 *
 */

@Entity
@Table(name="HIERARCHY_MASTER")
public class Hierarchy {
	/**
	 * School of a person
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Index(name = "hid")
    private Long hid;
	

	@Column(name="SCHOOL")
	private String school;

	/**
	 * Institution of a person
	 */
	@Column(name="INSTITUTION")
//	@Index(name = "institution")
	private String institution;

	/**
	 * Time_Stamp is the Current time of person 
	 */
	
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	
	@Lob @Column(name = "SCHOOL_LOGO", length = 400000)
	private byte[] schoolLogo;
	
	
	@Column(name="SCHOOL_ADDRESS1")
	private String schoolAddress1;
	
	
	@Column(name="SCHOOL_ADDRESS2")
	private String schoolAddress2;
	
	
	@Column(name="PINCODE")
	private String pincode;
	
	
	@Column(name="LANDLINE_NUMBER")
	private String landLineNumber;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="UPDATE_TIME")
	private String updateTime;
	
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="SCHOOL_PHONE_NUMBER")
	private String schoolPhoneNumber;
	
	
	
	public String getLandLineNumber() {
		return landLineNumber;
	}

	public void setLandLineNumber(String landLineNumber) {
		this.landLineNumber = landLineNumber;
	}

	

	public byte[] getSchoolLogo() {
		return schoolLogo;
	}

	public void setSchoolLogo(byte[] schoolLogo) {
		this.schoolLogo = schoolLogo;
	}

	public String getSchoolAddress1() {
		return schoolAddress1;
	}

	public void setSchoolAddress1(String schoolAddress1) {
		this.schoolAddress1 = schoolAddress1;
	}

	public String getSchoolAddress2() {
		return schoolAddress2;
	}

	public void setSchoolAddress2(String schoolAddress2) {
		this.schoolAddress2 = schoolAddress2;
	}



	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getSchoolPhoneNumber() {
		return schoolPhoneNumber;
	}

	public void setSchoolPhoneNumber(String schoolPhoneNumber) {
		this.schoolPhoneNumber = schoolPhoneNumber;
	}

	/**
	 * Gets the current time  of the person
	 * @return this person's(current timestam) Time_Stamp
	 */
	

	
	

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	/**
	 * Gets the name of the institution
	 * @return institution, current name of the institution
	 */

	public String getInstitution() {
		return institution;
	}

	/**
	 * Changes the name of the institution
	 * @param institution, new name of the institution
	 */

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	/**
	 * Gets the name of the school
	 * @return school, current name of the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * Changes the name of the school
	 * @param school, new name of the school
	 */

	public void setSchool(String school) {
		this.school = school;
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



	public Long getHid() {
		return hid;
	}

	public void setHid(Long hid) {
		this.hid = hid;
	}
	


}






