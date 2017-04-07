package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.NoticeDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class NoticeServiceImpl implements NoticeService
{
	@Autowired
	NoticeDao noticeDao;
	@Autowired
	ProfileDao profileDao;

	@Override
	public void addNotice(Notice notice,Token token) throws Exception {
		noticeDao.addNotice(notice,token);
		
	}

	@Override
	public List<Notice> viewNotice(ArrayList<String> smartIdList,Long hid) throws Exception{
		return noticeDao.viewNotice(smartIdList,hid);
		
	}

	@Override
	public void deleteNotice(Notice notice) throws Exception {
		 noticeDao.deleteNotice(notice);
		
	}

	@Override
	public void editNotice(Notice notice) throws Exception {
		noticeDao.editNotice(notice);
		
	}
	

	@Override
	public List<Notice> viewGenericNotice(String type){
		 
		return noticeDao.viewGenericNotice(type);
	}
	
	@Override
	public List<Notice> viewMyNotice(String role, Long hid) {
		return noticeDao.viewMyNotice(role,hid);
	}

	
	@Override
	public ArrayList<Profile> getAllProfiles() {
		ArrayList<Profile> profileList = profileDao.getAllProfiles();
		return profileList;
	}
	
//	@Override
//	public ArrayList<Profile> getProfiles(String role,String smartId) throws GSmartServiceException {
//		
//		ArrayList<Profile> profileList = profileDao.getProfiles(role,smartId);
//		return profileList;
//	}


	@Override
	public Map<String, Object> getParentInfo(String empSmartId) {
		Map<String, Object> parentInfo = new HashMap<>();
		Profile parentProfile = profileDao.getParentInfo(empSmartId);
		parentInfo.put("parentProfile", parentInfo);
		String parentSmartId = parentProfile.getSmartId();
		parentInfo.put("reportingProfiles", profileDao.getReportingProfiles(parentSmartId));
		return parentInfo;
	}

	@Override
	public Profile getProfileDetails(String empSmartId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}