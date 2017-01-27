package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface ProfileServices{
	
	/*for registration*/
	
	public String getmaxSamrtId()throws GSmartServiceException;
	
	public boolean insertUserProfileDetails(Profile profile)throws GSmartServiceException;

	public String updateProfile(Profile profile)throws GSmartServiceException;


	/*for profile*/

	public ArrayList<Profile> getProfiles(String role,String smartId)throws GSmartServiceException;

	public ArrayList<Profile> getAllProfiles()throws GSmartServiceException;
	
	public Map<String, Object> getParentInfo(String empSmartId)throws GSmartServiceException;

	/*for login*/
	
	Profile getProfileDetails(String smartId)throws GSmartServiceException;
	
	List<Profile> search( Profile profileList)throws GSmartServiceException;

	public void editRole(Profile profile)throws GSmartServiceException;
	
	List<Profile> getProfileByHierarchy(Hierarchy hierarchy)throws GSmartServiceException;
	public List<Profile> getProfilesWithoutRfid()throws GSmartDatabaseException;
	
	public List<Profile> getProfilesWithRfid()throws GSmartDatabaseException;
	
	public List<Profile> addRfid(Profile rfid)throws GSmartServiceException;
	
	public List<Profile> editRfid(Profile rfid)throws GSmartServiceException;

}
