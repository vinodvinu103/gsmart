package com.gsmart.services;

import java.util.List;
import com.gsmart.util.GSmartServiceException;

import com.gsmart.model.Notice;

public interface NoticeService {
	
	public List<Notice> addNotice(Notice notice)throws GSmartServiceException;

	public List<Notice> viewAllNotice()throws GSmartServiceException;
	
    public  void deleteNotice(Notice notice) throws GSmartServiceException;
    
    public  List<Notice> editNotice(Notice notice) throws GSmartServiceException;

    public List<Notice> viewSpecificNotice(Integer smart_id)throws Exception;
  
/*    public  List<Notice> viewAllNotice()throws Exception;
    
    public List<Notice> childNotice(String smartId) throws Exception;

*/

}
