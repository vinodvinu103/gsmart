package com.gsmart.dao;

import java.util.List;
import com.gsmart.util.GSmartDatabaseException;

import com.gsmart.model.Notice;

public interface NoticeDao {
	
	public List<Notice> addNotice(Notice notice)throws GSmartDatabaseException;

	public List<Notice> viewAllNotice()throws GSmartDatabaseException;

    public  void deleteNotice(Notice notice) throws GSmartDatabaseException;

    public  List<Notice> editNotice(Notice notice) throws GSmartDatabaseException;
    
    public List<Notice> viewSpecificNotice(Integer smart_id)throws Exception;

/*	public List<Notice> viewAllNotice()throws Exception;
	
	public List<Notice> childNotice(String smartId)throws Exception;
*/
}
