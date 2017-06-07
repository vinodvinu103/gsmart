package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Banners;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface ProfileServices{
	
	/*for registration*/
	
	public String getmaxSamrtId()throws GSmartServiceException;
	
	public boolean insertUserProfileDetails(Profile profile)throws GSmartServiceException;

	public String updateProfile(Profile profile)throws GSmartServiceException;
	
	public String deleteprofile(Profile profile)throws GSmartServiceException;
	
	/* for profile image change*/
	
	public String changeprofileimage(Profile profile)throws GSmartServiceException;
	
	/*for profile*/

	public Map<String, Object> getProfiles(String role,String smartId,Long hid, int min, int max)throws GSmartServiceException;

/*	public ArrayList<Profile> getAllProfiles()throws GSmartServiceException;*/
	
	public Map<String, Object> getParentInfo(String empSmartId)throws GSmartServiceException;

	/*for login*/
	
	Profile getProfileDetails(String smartId)throws GSmartServiceException;
	
	List<Profile> search( Profile profileList, Hierarchy hierarchy)throws GSmartServiceException;

	public void editRole(Profile profile)throws GSmartServiceException;
	
	List<Profile> getProfileByHierarchy(Hierarchy hierarchy)throws GSmartServiceException;
	
	List<Profile> getProfileByStuentHierarchy(Hierarchy hierarchy,String reportingManagerId) throws GSmartDatabaseException;

	public Map<String, Object> getProfilesWithoutRfid(Integer min, Integer max,Long hierarchy)throws GSmartDatabaseException;
	
	public Map<String, Object> getProfilesWithRfid(Integer min, Integer max,Long hierarchy)throws GSmartDatabaseException;
	
	public List<Profile> addRfid(Profile rfid)throws GSmartServiceException;
	
	public List<Profile> editRfid(Profile rfid)throws GSmartServiceException;
	
	public List<Profile> searchProfilesWithoutRfid(String profileList,String role,Hierarchy hierarchy) throws GSmartServiceException;

	public List<Profile> searchProfilesWithRfid(String profileList,String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	public void addBanner(Banners banner) throws GSmartServiceException;

	public List<Banners> getBannerList() throws GSmartServiceException;
	/**
	 * @param banner instanceOf {@link Bannners}
	 * @return nothing 
	 * @throws GSmartServiceException
	 */
	/*public Banners editBanner(Banners banner) throws GSmartServiceException;*/
	
	/**
	 * @param banner instanceOf {@link Banners}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deleteBanner(Banners banner)throws GSmartServiceException;

	public List<Profile> searchemp(Profile profile, Long hid)throws GSmartServiceException;

	public List<Profile> searchstudent(Profile profile, Long hid)throws GSmartServiceException;

}
