package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface NoticeDao{
	
	public void addNotice(Notice notice,Token token)throws Exception;

	public List<Notice> viewNotice(ArrayList<String> smartIdList)throws Exception;

    public  void deleteNotice(Notice notice) throws Exception;

    public  Notice editNotice(Notice notice) throws Exception;

	public List<Notice> viewSpecificNotice(String role);
	
	public List<Notice> viewMyNotice(String role);
	
	public ArrayList<Profile> getProfiles(String role,String smartId)throws GSmartServiceException;

	public ArrayList<Profile> getAllProfiles()throws GSmartServiceException;
	
	public Profile getParentInfo(String empSmartId)throws GSmartServiceException;

	/* for login */	
	public Profile getProfileDetails(String empSmartId);

	public List<Profile> getAllRecord();


    
   // public List<Notice> viewSpecificNotice(Integer smart_id)throws Exception;

	//public List<Notice> viewAllNotice()throws Exception;
	
	//public List<Notice> childNotice(String smartId)throws Exception;
}
