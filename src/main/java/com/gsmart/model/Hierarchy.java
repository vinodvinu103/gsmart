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
@IdClass( com.gsmart.model.CompoundHierarchy.class)

public class Hierarchy {
	/**
	 * School of a person
	 */
	
	@Id
	@Column(name="SCHOOL")
	private String school;

	/**
	 * Standard of a person
	 */

//	@Id
//	@Column(name="STANDARD")
//	private String standard;
//
//	/**
//	 * Section of a person
//	 */
//	@Id
//	@Column(name="SECTION")
//	private String section;

	/**
	 * Institution of a person
	 */
	@Id
	@Column(name="INSTITUTION")
	private String institution;

	/**
	 * Time_Stamp is the Current time of person 
	 */
	
	@Column(name="ENTRY_TIME")
	private String entryTime;

	/**
	 * Gets the current time  of the person
	 * @return this person's(current timestam) Time_Stamp
	 */
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="UPDATE_TIME")
	private String updateTime;
	
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	

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

	/**
	 * Gets the name of the standard
	 * @return standard, current name of the standard
	 */

	/*public String getStandard() {
		return standard;
	}*/
	/**
	 * Changes the name of the standard
	 * @param standard, new name of the standard
	 */
	/*public void setStandard(String standard) {
		this.standard = standard;
	}*/
	/**
	 * Gets the name of the section
	 * @return section, current name of the section
	 */

	/*public String getSection() {
		return section;
	}*/

	/**
	 * Changes the name of the section
	 * @param section, new name of the section
	 */
	/*public void setSection(String section) {
		this.section = section;
	}*/

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
	public String toString( ) {
		return "\n\n{Hierarchy {Compound  : \n\tschool=" + school + ",\n\t institution="
				+ institution + " },\n entryTime=" + entryTime + ",\n exitTime=" + exitTime + ",\n updateTime=" + updateTime
				+ ",\n isActive=" + isActive + " }";
	}
	


}






