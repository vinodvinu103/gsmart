package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

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

	/* for profile */

	@Override
	public ArrayList<Profile> getAllProfiles() {
		ArrayList<Profile> profileList = profileDao.getAllProfiles();
		return profileList;
	}
	
	@Override
	public ArrayList<Profile> getProfiles(String role,String smartId) throws GSmartServiceException {
		
		ArrayList<Profile> profileList = profileDao.getProfiles(role,smartId);
		return profileList;
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



}
