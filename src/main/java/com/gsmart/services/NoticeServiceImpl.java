package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.NoticeDao;
import com.gsmart.model.Notice;

@Service
public class NoticeServiceImpl implements NoticeService
{
	@Autowired
	NoticeDao noticeDao;

	@Override
	public void addNotice(Notice notice) throws Exception {
		noticeDao.addNotice(notice);
		
	}

	@Override
	public List<Notice> viewNotice() throws Exception{
		return noticeDao.viewNotice();
		
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
	public List<Notice> viewSpecificNotice(Integer smart_id) throws Exception{
		 
		return noticeDao.viewSpecificNotice(smart_id);
	}

	@Override
	public List<Notice> viewAllNotice()throws Exception {
		
		return noticeDao.viewAllNotice();
	}
	

}
