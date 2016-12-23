/*
* class name: CompoundHierarchy.java

 *
 * Copyright (c) 2008-2009 Gowdanar Technologies Pvt. Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Gowdanar
 * Technologies Pvt. Ltd.("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Gowdanar Technologie.
 *
 * GOWDANAR TECHNOLOGIES MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GOWDANAR TECHNOLOGIES SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING
 */

package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
/**
 * class-name: CompoundHierarchy.java
 * Assigning hierarchy for everyone 
 * from the institution to the section
 *   
 * @author 
 * @version 1.0
 * @since 2016-02-23  
 *
 */
@Embeddable

public class CompoundHierarchy  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * institution of a person
	 */
	private String institution;
	/**
	 * School of a person
	 */
	private String school;
	/**
	 * standard of a person
	 */
	private String standard;
	/**
	 * section of a person
	 */
	private String  section;
	
	private String entryTime;
	
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
	 * Gets the name of the School
	 * @return School, current name of the School
	 */
	public String getSchool() {
		return school;
	}
	/**
	 * Changes the name of the School
	 * @param School, new name of the School
	 */
	public void setSchool(String school) {
		this.school = school;
	}
	/**
	 * Gets the name of the Standard
	 * @return Standard, current name of the Standard
	 */
	public String getStandard() {
		return standard;
	}
	/**
	 * Changes the name of the Standard
	 * @param Standard, new name of the Standard
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}
	/**
	 * Gets the name of the Section
	 * @return Section, current name of the Section
	 */
	public String getSection() {
		return section;
	}
	/**
	 * Changes the name of the Section
	 * @param Section, new name of the Section
	 */
	public void setSection(String section) {
		this.section = section;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	
}