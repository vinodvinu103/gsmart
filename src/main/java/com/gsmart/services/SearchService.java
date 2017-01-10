package com.gsmart.services;

import java.util.ArrayList;
import java.util.Map;

//import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.util.GSmartServiceException;

public interface SearchService {
	
	Map<String, Profile> getAllProfiles();
	
	ArrayList<Profile> getEmployeeInfo(String emp, Map<String, Profile> map);

	ArrayList<Profile> searchEmployeeInfo(String smartId, Map<String, Profile> beanFactory1);

	Map<String, Profile> searchRep(Search search);

	ArrayList<String> searchParentInfo(String smartId, Map<String, Profile> map);
	
	//List<Notice> searchSpecificNotice(ArrayList<String> parentList,List<Notice>notices);
	
	ArrayList<Profile> studentFees(ArrayList<Profile> profile) throws GSmartServiceException;
	
	ArrayList<Profile> gotoloop(ArrayList<Profile> profile ,Map<String, Profile> profiles) throws GSmartServiceException;
	
	ArrayList<Profile> sumUpFee(ArrayList<Profile> childList,Map<String, Profile> profiles) throws GSmartServiceException;

	ArrayList<Profile> totalfees(Map<String, Profile> profileMap, ArrayList<Profile> fees);
	
	public Map<String, Object> getParentInfo(String smartId);
}
