/*
 * class name: ProfileDao.java
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
package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
//import com.gsmart.model.Search;
import com.gsmart.util.GSmartDatabaseException;

/**
 * This is the Profile DAO.
 * 
 * @version 1.10 06 Jan 2010
 * @author Shakti Panigrahi
 */
public interface ProfileDao {
	
	/* for registration */

	public String getMaxSmartId();

	public boolean userProfileInsert(Profile profile);

	public String updateProfile(Profile profile);

	/* for profile */
	public ArrayList<Profile> getAllProfiles();

	public ArrayList<Profile> getProfiles(String role,String smartId,String role2,Hierarchy hierarchy);

	public Profile getParentInfo(String empSmartId);

	public ArrayList<Profile> getReportingProfiles(String parentSmartId);

	/* for login */	
	public Profile getProfileDetails(String empSmartId);

	public List<Profile> getAllRecord(String academicYear,String role,Hierarchy  hierarchy);

	
	/**
	 * @return list of Profile entities available in the {@link Profile} Table
	 * @throws GSmartDatabaseException
	 */
	public List<Profile> search(Profile profile) throws GSmartDatabaseException;

	
	/**
	 * @param profile instanceOf {@link Profile}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */
	public void editRole(Profile profile)throws GSmartDatabaseException;

	public List<Profile> getsearchRep(Search search);
	
	public List<Profile> getProfileByHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException;

	public List<Profile> getProfilesWithoutRfid()throws GSmartDatabaseException;
	
	public List<Profile> addRfid(Profile rfid)throws GSmartDatabaseException;
//	
	public List<Profile> getProfilesWithRfid()throws GSmartDatabaseException;
	
	public List<Profile> editRfid(Profile rfid)throws GSmartDatabaseException;
	
}
