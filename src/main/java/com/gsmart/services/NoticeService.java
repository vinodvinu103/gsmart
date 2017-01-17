package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Notice;

public interface NoticeService {
	
	public void addNotice(Notice notice)throws Exception;

	public List<Notice> viewNotice()throws Exception;
	
    public  void deleteNotice(Notice notice) throws Exception;
    
    public  void editNotice(Notice notice) throws Exception;


    public List<Notice> viewSpecificNotice(Integer smart_id)throws Exception;
  
    public  List<Notice> viewAllNotice()throws Exception;


}
