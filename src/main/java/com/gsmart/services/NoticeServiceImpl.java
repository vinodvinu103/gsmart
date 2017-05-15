package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.NoticeDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService
{
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private ProfileDao profileDao;

	@Override
	public void addNotice(Notice notice,Token token) throws Exception {
		noticeDao.addNotice(notice,token);
		
	}

	@Override
	public List<Notice> viewNotice(ArrayList<String> smartIdList,Long hid) throws Exception{
		Loggers.loggerStart("viewNotice started in notice service for hierarchyId :"+hid+" with smardIdList count of "+smartIdList.size());
		List<Notice> notices=null;
		try {
			notices= noticeDao.viewNotice(smartIdList,hid);
			
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd("viewNotice ended in notice service for hierarchyId :"+hid+" with smardIdList count of "+smartIdList.size());
		return notices;
		
		
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
	public List<Notice> viewGenericNotice(String type) throws GSmartServiceException{
		Loggers.loggerStart("viewGenericNotice started in notice service of type :"+type);
		List<Notice> notices=null;
		try {
			notices= noticeDao.viewGenericNotice(type);
			
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw new GSmartServiceException( exception.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd("viewGenericNotice ended in notice service of type :"+type);
		return notices; 
	}
	
	@Override
	public List<Notice> viewMyNotice(String smartId, Long hid) throws GSmartServiceException{
		Loggers.loggerStart("viewMyNotice api started in notice service for id :" + smartId +" with hierarchyId of "+hid);
		List<Notice> notices=null;
		try {
			notices=noticeDao.viewMyNotice(smartId,hid);
			
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd("viewMyNotice ended in notice service for id :"+smartId+" with hierarchyId of "+hid+"  myNoticesList count of "+notices.size());
		return notices;
	}

	@Override 
	public List<Notice> viewAdminNoticeService(String SmartId) throws GSmartServiceException{
		Loggers.loggerStart("viewAdminNoticeService started in notice service for id :"+SmartId);
		List<Notice> notices=null;
		try {
			notices=noticeDao.viewAdminNoticeDao(SmartId);
			
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd("viewAdminNoticeService ended in notice service for id :"+SmartId+" with noticesList count of "+notices.size());
		return notices;
	}
	/*@Override
	public ArrayList<Profile> getAllProfiles() {
		ArrayList<Profile> profileList = profileDao.getAllProfiles();
		return profileList;
	}*/
	
//	@Override
//	public ArrayList<Profile> getProfiles(String role,String smartId) throws GSmartServiceException {
//		
//		ArrayList<Profile> profileList = profileDao.getProfiles(role,smartId);
//		return profileList;
//	}


	@Override
	public Map<String, Object> getParentInfo(String empSmartId) throws GSmartServiceException {
		Map<String, Object> parentInfo = new HashMap<>();
		Profile parentProfile;
		try {
			parentProfile = profileDao.getParentInfo(empSmartId);
			parentInfo.put("parentProfile", parentInfo);
			String parentSmartId = parentProfile.getSmartId();
			parentInfo.put("reportingProfiles", profileDao.getReportingProfiles(parentSmartId));
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		
		return parentInfo;
	}

	@Override
	public Profile getProfileDetails(String empSmartId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}