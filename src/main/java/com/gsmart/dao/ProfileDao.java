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
import java.util.Map;


import com.gsmart.model.Banners;
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
	

	public boolean userProfileInsert(Profile profile) throws GSmartDatabaseException;

	public String updateProfile(Profile profile);
	
	public String deleteprofile(Profile profile);
	
	public boolean deleteProfileIfMailFailed(String smartId);
	
	/* for profile image change*/
	public String changeprofileimage(Profile profile);
	
	/* for profile */
	public ArrayList<Profile> getAllProfiles(String AcademicYear) throws GSmartDatabaseException;

	public Map<String, Object> getProfiles(String role, String smartId,Long hid, int min,
			int max) throws GSmartDatabaseException;

	public Profile getParentInfo(String empSmartId) throws GSmartDatabaseException;

	public ArrayList<Profile> getReportingProfiles(String parentSmartId);

	/* for login */
	public Profile getProfileDetails(String empSmartId);

	public List<Profile> getAllRecord(String academicYear, Long hid);

	/**
	 * @return list of Profile entities available in the {@link Profile} Table
	 * @throws GSmartDatabaseException
	 */
	public List<Profile> search(Profile profile, Hierarchy hierarchy) throws GSmartDatabaseException;
	
	/**
	 * @param profile
	 *            instanceOf {@link Profile}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public void editRole(Profile profile)throws GSmartDatabaseException;

	public List<Profile> getsearchRep(Search search,String role,Hierarchy hierarchy);


	public List<Profile> getProfileByHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public List<Profile> getProfileByStuentHierarchy(Hierarchy hierarchy,String reportingManagerId) throws GSmartDatabaseException;

	public Map<String, Object> getProfilesWithoutRfid(Integer min, Integer max,Long hierarchy)throws GSmartDatabaseException;

	public Map<String, Object> addRfid(Profile rfid)throws GSmartDatabaseException;
	
	public Map<String, Object> getProfilesWithRfid(Integer min, Integer max,Long hierarchy)throws GSmartDatabaseException;
	
	public List<Profile> editRfid(Profile rfid)throws GSmartDatabaseException;
	
	public List<Profile> searchProfilesWithoutRfid(String profile,String role,Hierarchy hierarchy) throws GSmartDatabaseException;

	public List<Profile> searchProfilesWithRfid(String profile,String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public void addBanner(Banners banner) throws GSmartDatabaseException;

	public List<Banners> getBannerList();
	
	/*public Banners editBanner(Banners banner) throws GSmartDatabaseException, Exception;*/

	public void deleteBanner(Banners banner)throws GSmartDatabaseException;

	public List<Profile> getProfileByHierarchyAndYear(Hierarchy hierarchy, String year);


	public List<Profile> searchemp(Profile profile, Long hid)throws GSmartDatabaseException;


	public List<Profile> searchstudent(Profile profile, Long hid)throws GSmartDatabaseException;


	public List<Profile> getProfilesOfNullHierarchy(String academicYear);


	public List<Profile> searchwithrfid(Profile profile, Long hid)throws GSmartDatabaseException;
	
	public List<Profile> searchwithoutrfid(Profile profile, Long hid)throws GSmartDatabaseException;
}
