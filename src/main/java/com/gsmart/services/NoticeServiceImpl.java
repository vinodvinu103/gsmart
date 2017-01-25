package com.gsmart.services;

import java.util.List;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.NoticeDao;
import com.gsmart.model.Notice;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	NoticeDao noticeDao;

	@Override
	public List<Notice> addNotice(Notice notice) throws GSmartServiceException {
		noticeDao.addNotice(notice);
		return null;
		
	}

	@Override
	public List<Notice> viewAllNotice() throws GSmartServiceException {
		Loggers.loggerStart();
		return noticeDao.viewAllNotice();
	
	}

	@Override
	public void deleteNotice(Notice notice) throws GSmartServiceException {
		noticeDao.deleteNotice(notice);
		
	}

	@Override
	public List<Notice> editNotice(Notice notice) throws GSmartServiceException {
		noticeDao.editNotice(notice);
		return null;
	}

	@Override
	public List<Notice> viewSpecificNotice(Integer smart_id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public List<Notice> viewAllNotice() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notice> childNotice(String smartId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}*/
	

}
