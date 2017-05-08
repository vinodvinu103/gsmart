package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface NoticeDao{
	
	public void addNotice(Notice notice,Token token)throws Exception;

	public List<Notice> viewNotice(ArrayList<String> smartIdList,Long hid)throws Exception;

    public  void deleteNotice(Notice notice) throws Exception;

    public  Notice editNotice(Notice notice) throws Exception;

	public List<Notice> viewGenericNotice(String type);
	
	public List<Notice> viewMyNotice(String role, Long hid);
	
	public ArrayList<Profile> getProfiles(String role,String smartId)throws GSmartServiceException;

	public ArrayList<Profile> getAllProfiles()throws GSmartServiceException;
	
	public Profile getParentInfo(String empSmartId)throws GSmartServiceException;

	/* for login */	
	public Profile getProfileDetails(String empSmartId);

	public List<Profile> getAllRecord();

	public List<Notice> viewNoticeForAdmin(Long hid)throws GSmartServiceException;
	
	public List<Notice> viewAdminNoticeDao(String SmartId)throws GSmartServiceException;
    
   // public List<Notice> viewSpecificNotice(Integer smart_id)throws Exception;

	//public List<Notice> viewAllNotice()throws Exception;
	
	//public List<Notice> childNotice(String smartId)throws Exception;
}
