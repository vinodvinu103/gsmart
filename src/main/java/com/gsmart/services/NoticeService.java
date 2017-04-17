package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface NoticeService {
	
	public void addNotice(Notice notice,Token token)throws Exception;

	public List<Notice> viewNotice(ArrayList<String> smartIdIList,Long hid)throws Exception;
	
    public  void deleteNotice(Notice notice) throws Exception;
    
    public  void editNotice(Notice notice) throws Exception;

	public List<Notice> viewGenericNotice(String type);
	
	public List<Notice> viewMyNotice(String role, Long hid);
	

	
//	public ArrayList<Profile> getProfiles(String role,String smartId)throws GSmartServiceException;

//	public ArrayList<Profile> getAllProfiles()throws GSmartServiceException;
	
	public Map<String, Object> getParentInfo(String empSmartId)throws GSmartServiceException;
	
	public Profile getProfileDetails(String empSmartId);
 

//    public List<Notice> viewSpecificNotice(Integer smart_id)throws Exception;
//  
//    public  List<Notice> viewAllNotice()throws Exception;
//    
//    public List<Notice> childNotice(String smartId) throws Exception;


}
