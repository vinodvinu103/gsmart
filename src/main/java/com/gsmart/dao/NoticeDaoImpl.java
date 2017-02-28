package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.OverridesAttribute;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Band;
import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Repository
public class NoticeDaoImpl implements NoticeDao {

	@Autowired
	SessionFactory sessionFactory;
	Session session;
	Transaction transaction;
	Query query=null;
	
	final Logger logger = Logger.getLogger(NoticeDao.class);
	
	public void getConnection(){
		session=sessionFactory.openSession();
		transaction=session.beginTransaction();
	}
	
	@Override
	public void addNotice(Notice notice,Token token){
		getConnection();
		Loggers.loggerStart();
	
		try{
			notice.setSmartId(token.getSmartId());
			notice.setRole(token.getRole());
			notice.setIsActive("Y");
			notice.setEntryTime(CalendarCalculator.getTimeStamp()); 
			session.save(notice);
			transaction.commit();
			Loggers.loggerEnd();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			session.close();
		}

	}

	@Override
	public List<Notice> viewNotice(ArrayList<String> smartIdList) {
		getConnection();
		Loggers.loggerStart();
		
		try{
			query=session.createQuery("FROM Notice where isActive='Y' and smartId in  (:smartIdList) and type='Specific' ORDER BY entryTime desc");
			query.setParameterList("smartIdList", smartIdList);

		Loggers.loggerValue("smartIdList", smartIdList);
			Loggers.loggerStart(smartIdList);

			//FROM UserDetails user ORDER BY user.userName DESC
			//query.setMaxResults(6);
			@SuppressWarnings("unchecked")
			List<Notice> notices=query.list();
			transaction.commit();
			Loggers.loggerEnd(notices);
			return notices;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
		
	}
	@Override
public List<Notice> viewMyNotice(String smartId) {
		Loggers.loggerStart();
		getConnection();
		Loggers.loggerStart();
		

		try{
			Loggers.loggerStart(smartId);
			query=session.createQuery("from Notice where is_active='Y' and smartId=:smartId ORDER BY entryTime desc");
			query.setParameter("smartId", smartId);
			//query.setMaxResults(6);
			@SuppressWarnings("unchecked")
			List<Notice> list=query.list();
			transaction.commit();
			Loggers.loggerEnd(list);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
	}

	@Override
	public void deleteNotice(Notice notice) {
		Loggers.loggerStart();
		getConnection();
		try{
		
//			notice= (Notice) session.get("com.gsmart.model.Notice",notice.getEntry_time());
			notice.setIsActive("D");
			notice.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(notice);
			transaction.commit();
			Loggers.loggerEnd();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			session.close();
		}
	}

	@Override
	public Notice editNotice(Notice notice)throws GSmartBaseException{
	
		getConnection();
		try{
			Loggers.loggerStart();
			
			Notice oldNotice = getNotice(notice.getEntryTime());
			oldNotice.setUpdate_time(CalendarCalculator.getTimeStamp());
			oldNotice.setIsActive("N");
			session.update(oldNotice);
			
			notice.setIsActive("Y");
			notice.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(notice);
			
			transaction.commit();
/*			
			Loggers.loggerEnd();
			addNotice(notice,"smartId");*/
			
		}catch (org.hibernate.exception.ConstraintViolationException e){
		}catch (Throwable e) {
			throw new GSmartBaseException(e.getMessage());
		}
		
		finally {
			session.close();
		}
	
	return notice;
	
	}
	
	public Notice getNotice(String entryTime){
      try{
    	  
			query = session.createQuery("from Notice where isActive='Y' and entryTime='" + entryTime + "' ORDER BY entryTime desc");
			@SuppressWarnings("unchecked")
			ArrayList<Notice> viewNotice = (ArrayList<Notice>) query.list();
			
			return viewNotice.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Notice> viewGenericNotice(String type) {
		Loggers.loggerStart();
		getConnection();
		Loggers.loggerStart();
		
		try{
			
			query=session.createQuery("from Notice where is_active='Y' and type='Generic' ORDER BY entryTime desc");
		//	query.setParameter("type", type);
			//query.setMaxResults(6);
			@SuppressWarnings("unchecked")
			List<Notice> list=query.list();
			transaction.commit();
			Loggers.loggerEnd(list);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getAllProfiles() {
		getConnection();
		try {
			
			query = session.createQuery("from Profile where isActive='Y'");
			return (ArrayList<Profile>) query.list();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
	}
		
		
	
	@SuppressWarnings("unchecked") 
	@Override
	public ArrayList<Profile> getProfiles(String role,String smartId) {
		getConnection();
		try {
	
			Loggers.loggerStart("current smartId"+smartId);
			
			if (role.toLowerCase().equals("student")) {
				query = session.createQuery("from Profile where isActive='Y'and role='student' and smartId like '"+smartId.substring(0,2)+"%'");
			} else {
				query = session.createQuery("from Profile where isActive='Y'and role!='student' and smartId like '"+smartId.substring(0,2)+"%'");
			}

			return (ArrayList<Profile>) query.list();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
	}
	
}

	@Override
	public Profile getParentInfo(String smartId) {
		    getConnection();
		try {
			
			Profile currentProfile = (Profile) session.createQuery("from Profile where smartId=" + smartId).list()
					.get(0);
			if (currentProfile.getReportingManagerId() != smartId)
				return getProfileDetails(currentProfile.getReportingManagerId());
			else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
	}
	
	
}



public Profile getProfileDetails(String smartId) {
	getConnection();
	Loggers.loggerStart(smartId);
	Profile profilelist = null;
	   
	try {
		
		query = session.createQuery("from Profile where isActive='Y' AND smartId= :smartId");
		query.setParameter("smartId", smartId);
		profilelist = (Profile) query.list().get(0);
		profilelist.setChildFlag(true);
		
	} catch (Exception e){
		e.printStackTrace();
	}
    
   finally {
	    session.close();
   }
	
	Loggers.loggerEnd(profilelist);
	return profilelist;

      
      }


     @SuppressWarnings("unchecked")
     @Override
          public List<Profile> getAllRecord() {
    		getConnection();   
    	 Loggers.loggerStart();
	
	     List<Profile> profile = null;
	
	       try {
	       
	        	query = session.createQuery("from Profile where isActive like('Y')");
	 	
     		profile = (List<Profile>) query.list();
        	} catch (Exception e) {
	    	e.printStackTrace();
	     	return null;
	} 
	       finally {
			session.close();
		}
	
	   Loggers.loggerEnd("profile fetched from DB");
     	return profile;
     
     	
    }

}

/*
	@Override
	public List<Notice> viewSpecificNotice(Integer smart_id) {
		try{
			getConnection();		
			query=session.createQuery("from Notice where smart_id="+smart_id+" and type='S'");		
			@SuppressWarnings("unchecked")
			List<Notice> notices=query.list();
			transaction.commit();
			return notices;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> viewAllNotice() {
		try {
			getConnection();
			query = session.createQuery("from Notice where is_active='Y'");
			transaction.commit();
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> childNotice(String smartId) throws Exception {
		Loggers.loggerStart(smartId);
		try {
           
			getConnection();
			query = session.createQuery("from Notice where is_active='Y'");
			List<Notice> notices=query.list();
			transaction.commit();
			return  notices;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
			
		
    }
}
*/