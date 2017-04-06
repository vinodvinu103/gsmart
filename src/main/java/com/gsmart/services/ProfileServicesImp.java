package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Banners;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class ProfileServicesImp implements ProfileServices {

	
	@Autowired
	private ProfileDao profileDao;

	/* for registration */

	@Override
	/**
	 * This is getmaxSmartId() method.
	 * 
	 * @return ArrayList of team Smart id
	 */

	public String getmaxSamrtId() {
		/*
		 * This getmaxSmartId() method will get maximum smart id from Emp_MASTER
		 * table and return to UserProfile Controller.
		 */
		try {
			return profileDao.getMaxSmartId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override

	/**
	 * This is insertUserProfileDetails() method.
	 * 
	 * @param userprofile
	 *            this is the UserProfile type and having new user profile
	 * @return boolean
	 */
	public boolean insertUserProfileDetails(Profile profile) {
		
		try {
			return profileDao.userProfileInsert(profile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String updateProfile(Profile profile) {

		return profileDao.updateProfile(profile);
	}

	@Override
	public String deleteprofile(Profile profile){
		return profileDao.deleteprofile(profile);
	}
	/* for profile */

	@Override
	public ArrayList<Profile> getAllProfiles() {
		ArrayList<Profile> profileList = profileDao.getAllProfiles();
		return profileList;
	}
	
	@Override
	public Map<String, Object> getProfiles(String role,String smartId,Long hid, int min, int max) throws GSmartServiceException {
		Loggers.loggerStart();
		return profileDao.getProfiles(role,smartId,hid, min, max);
	}


	@Override
	public Map<String, Object> getParentInfo(String empSmartId) {
		Map<String, Object> parentInfo = new HashMap<>();
		Profile parentProfile = profileDao.getParentInfo(empSmartId);
		parentInfo.put("parentProfile", parentInfo);
		String parentSmartId = parentProfile.getSmartId();
		parentInfo.put("reportingProfiles", profileDao.getReportingProfiles(parentSmartId));
		return parentInfo;
	}

	/* for login */

	@Override

	/**
	 * This is getProfileDetails() method.
	 * 
	 * @param smartId
	 * @return ArrayList of profile
	 */
	public Profile getProfileDetails(String smartId) {
		/*
		 * This getProfileDetails() method will fetch all the Profile details
		 * from the Profile DAO and return to Profile Controller.
		 */
		return profileDao.getProfileDetails(smartId);
	}
	
	
	/**
	 * @return calls {@link ProfileDao}'s <code>search()</code> method
	 * @param profileList an instance of {@link Profile} class
	 */	

	@Override
	public List<Profile> search(Profile profileList) throws GSmartServiceException {
		try {
			return profileDao.search(profileList);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

	}
	
	/**
	 * calls {@link ProfileDao}'s <code>editRole(...)</code> method
	 * 
	 * @param profile
	 *            an instance of {@link Profile} class
	 * @throws GSmartServiceException
	 */
	
	@Override
	public void editRole(Profile profile) throws GSmartServiceException {
		try {
			profileDao.editRole(profile);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public List<Profile> getProfileByHierarchy(Hierarchy hierarchy) throws GSmartServiceException {
		return profileDao.getProfileByHierarchy(hierarchy);
	}


	public Map<String, Object> getProfilesWithoutRfid(Integer min, Integer max,Hierarchy hierarchy) throws GSmartDatabaseException {
	
		return profileDao.getProfilesWithoutRfid(min, max,hierarchy);
	}

	@Override
	public List<Profile> addRfid(Profile profile)throws GSmartServiceException {
		
		
		try {
			profileDao.addRfid(profile);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		return null;
		
	}

	public List<Profile> editRfid(Profile rfid)throws GSmartServiceException{
	
		try {
			profileDao.editRfid(rfid);
			} catch (GSmartDatabaseException exception) {
		throw (GSmartServiceException) exception;
		} catch (Exception e) {
		throw new GSmartServiceException(e.getMessage());
		}
		return null;
	}

	@Override
	public Map<String, Object> getProfilesWithRfid(Integer min, Integer max,Hierarchy hierarchy) throws GSmartDatabaseException {
		
		return profileDao.getProfilesWithRfid(min, max,hierarchy);
	}
	
	@Override
	public List<Profile> searchProfilesWithoutRfid(String profileListWithoutRfid,String role,Hierarchy hierarchy) throws GSmartServiceException {
		try {
			return profileDao.searchProfilesWithoutRfid(profileListWithoutRfid,role,hierarchy);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

	}

	
	@Override
	public List<Profile> searchProfilesWithRfid(String profileListWithRfid,String role,Hierarchy hierarchy) throws GSmartServiceException {
		try {
			return profileDao.searchProfilesWithRfid(profileListWithRfid,role,hierarchy);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}
	

	@Override
	public void addBanner(Banners banner) throws GSmartServiceException {
		profileDao.addBanner(banner);
	}
	@Override
	public List<Banners> getBannerList() throws GSmartServiceException {
		// TODO Auto-generated method stub
		return profileDao.getBannerList();
	}

	/*@Override
>>>>>>> 5a605812816b13c3ad7139025cd14377f05faa9e
	public Banners editBanner(Banners banner) throws GSmartServiceException {
		Loggers.loggerStart();
		Banners banners=null;
		try {
			banners=profileDao.editBanner(banner);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return banners;
	}*/
	@Override
	public void deleteBanner(Banners banner) throws GSmartServiceException {
		
		Loggers.loggerStart();
		try {
			profileDao.deleteBanner(banner);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}
}
