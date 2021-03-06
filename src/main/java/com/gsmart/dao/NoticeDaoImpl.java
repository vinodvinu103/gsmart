package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class NoticeDaoImpl implements NoticeDao {

	@Autowired
	private SessionFactory sessionFactory;
	private Query query=null;
	
	
	/*public void getConnection(){
		session=sessionFactory.openSession();
		transaction=session.beginTransaction();
	}*/
	
	@Override
	public void addNotice(Notice notice,Token token){
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
	
		try{
			notice.setSmartId(token.getSmartId());
			notice.setRole(token.getRole());
			notice.setIsActive("Y");

			notice.setEntryTime(CalendarCalculator.getTimeStamp()); 
			session.save(notice);
			Loggers.loggerEnd();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> viewNotice(ArrayList<String> smartIdList,Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart("viewNotice started in notice Dao for hierarchyId :"+hid+" with smardIdList count of "+smartIdList.size());
		List<Notice> notices=null;
		try{
			query=sessionFactory.getCurrentSession().createQuery("FROM Notice where isActive='Y' and smartId in  (:smartIdList) and hid=:hid and type='Specific' ORDER BY entryTime desc");
			query.setParameterList("smartIdList", smartIdList);
			query.setParameter("hid", hid);

			//FROM UserDetails user ORDER BY user.userName DESC
			//query.setMaxResults(6);
			notices=query.list();
			
		}catch (ConstraintViolationException e) {
 			e.printStackTrace();
 			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
 		}catch (NullPointerException e) {
 			e.printStackTrace();
 			throw new GSmartDatabaseException(Constants.NULL_PONITER);
 		}catch (Exception e) {
 			e.printStackTrace();
 			throw new GSmartDatabaseException(e.getMessage());
 		} 
		Loggers.loggerEnd("viewNotice ended in notice service for hierarchyId :"+hid+" with notices count of "+notices.size());
		return notices;
		
		
	}
	@SuppressWarnings("unchecked")
	@Override
public List<Notice> viewMyNotice(String smartId, Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart("viewMyNotice api started in notice Dao for id :" + smartId +" with hierarchyId of "+hid);
		List<Notice> list=null;

		try{
			Loggers.loggerStart(smartId);
			query=sessionFactory.getCurrentSession().createQuery("from Notice where is_active='Y' and smartId=(:smartId) and hid=:hid ORDER BY entryTime desc");
			query.setParameter("smartId", smartId);
			query.setParameter("hid", hid);
			//query.setMaxResults(6);
			list=query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		Loggers.loggerEnd("viewMyNotice ended in notice Dao for id :"+smartId+" with hierarchyId of "+hid+"  myNoticesList count of "+list.size());
		return list;
	}

	@Override
	public void deleteNotice(Notice notice) {
		Loggers.loggerStart(notice);
		Session session=this.sessionFactory.getCurrentSession();
		try{
		
//			notice= (Notice) session.get("com.gsmart.model.Notice",notice.getEntry_time());
			notice.setIsActive("D");
			notice.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(notice);
			Loggers.loggerEnd();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public Notice editNotice(Notice notice)throws GSmartBaseException{
		Loggers.loggerStart(notice);
	
		Session session=this.sessionFactory.getCurrentSession();
		try{
			Notice oldNotice = getNotice(notice.getEntryTime());
			oldNotice.setUpdateTime(CalendarCalculator.getTimeStamp());
			oldNotice.setIsActive("N");
			session.update(oldNotice);
			
			notice.setIsActive("Y");
			notice.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(notice);
			
/*			
			Loggers.loggerEnd();
			addNotice(notice,"smartId");*/
			
		}catch (ConstraintViolationException e){
		}catch (Throwable e) {
			throw new GSmartBaseException(e.getMessage());
		}
		
	Loggers.loggerEnd();
	return notice;
	
	}
	
	public Notice getNotice(String entryTime){
      try{
    	  Loggers.loggerStart(entryTime);
    	  
			query = sessionFactory.getCurrentSession().createQuery("from Notice where isActive='Y' and entryTime='" + entryTime + "' ORDER BY entryTime desc");
			@SuppressWarnings("unchecked")
			ArrayList<Notice> viewNotice = (ArrayList<Notice>) query.list();
			
			Loggers.loggerEnd();
			return viewNotice.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> viewGenericNotice(String type) throws GSmartDatabaseException{
		Loggers.loggerStart("viewGenericNotice api started in notice Dao  of type " + type);
		List<Notice> list=null;
		try{
			
			query=sessionFactory.getCurrentSession().createQuery("from Notice where is_active='Y' and type='Generic' ORDER BY entryTime desc");
		//	query.setParameter("type", type);
			//query.setMaxResults(6);
			list=query.list();
			
		}catch (ConstraintViolationException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}catch (NullPointerException e) {
			throw new GSmartDatabaseException(Constants.NULL_PONITER);
		}catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd("viewGenericNotice api started in notice Dao  of type " + type+" wit list size of "+list.size());
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getAllProfiles() {
		try {
			
			query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y'");
			return (ArrayList<Profile>) query.list();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
		
		
	
	@SuppressWarnings("unchecked") 
	@Override
	public ArrayList<Profile> getProfiles(String role,String smartId) {
		try {
	
			Loggers.loggerStart("current smartId"+smartId);
			
			if (role.equalsIgnoreCase("student")) {
				query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y'and role='student' and smartId like '"+smartId.substring(0,2)+"%'");
			} else {
				query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y'and role!='student' and smartId like '"+smartId.substring(0,2)+"%'");
			}

			return (ArrayList<Profile>) query.list();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	
}

	@Override
	public Profile getParentInfo(String smartId) {
		try {
			
			Profile currentProfile = (Profile) sessionFactory.getCurrentSession().createQuery("from Profile where smartId=" + smartId).list()
					.get(0);
			if (currentProfile.getReportingManagerId().equals(smartId))
				return getProfileDetails(currentProfile.getReportingManagerId());
			else
				return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
	
	
}



public Profile getProfileDetails(String smartId) {
	Loggers.loggerStart(smartId);
	Profile profilelist = null;
	   
	try {
		
		query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y' AND smartId= :smartId");
		query.setParameter("smartId", smartId);
		profilelist = (Profile) query.list().get(0);
		profilelist.setChildFlag(true);
		
	} catch (Exception e){
		e.printStackTrace();
	}
    
	
	Loggers.loggerEnd(profilelist);
	return profilelist;

      
      }


     @SuppressWarnings("unchecked")
     @Override
          public List<Profile> getAllRecord() {
    	 Loggers.loggerStart();
	
	     List<Profile> profile = null;
	
	       try {
	       
	        	query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive like('Y')");
	 	
     		profile = (List<Profile>) query.list();
        	} catch (Exception e) {
	    	e.printStackTrace();
	     	return null;
	} 
	       
	
	   Loggers.loggerEnd("profile fetched from DB");
     	return profile;
     
     	
    }
     
     @SuppressWarnings("unchecked")
	@Override
    public List<Notice> viewNoticeForAdmin(Long hid) throws GSmartServiceException {
    	Loggers.loggerStart(hid);
    	List<Notice> list=new ArrayList<>();
    	try {
			query=sessionFactory.getCurrentSession().createQuery("from Notice where hierarchy.hid=:hid and isActive='Y'");
			query.setParameter("hid",hid);
			list=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
       	Loggers.loggerEnd(list);
    	return list;
    }
    
     
     @SuppressWarnings("unchecked")
	@Override
     public List<Notice> viewAdminNoticeDao(String smartId)  throws GSmartServiceException {
    	 Loggers.loggerStart("viewAdminNoticeDao started in notice Dao for id :"+smartId);
    	 List<Notice> list = new ArrayList<>();
    	 try{
    		 query=sessionFactory.getCurrentSession().createQuery("from Notice where smartId= :smartId and isActive='Y' ");
    		 query.setParameter("smartId", smartId);
    		 list=query.list();
    	 }catch (ConstraintViolationException e) {
 			e.printStackTrace();
 			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
 		}catch (NullPointerException e) {
 			e.printStackTrace();
 			throw new GSmartDatabaseException(Constants.NULL_PONITER);
 		}catch (Exception e) {
 			e.printStackTrace();
 			throw new GSmartDatabaseException(e.getMessage());
 		} 
    	 Loggers.loggerEnd("viewAdminNoticeDao started in notice Dao for id :"+smartId+" with list size of "+list.size());
    	 return list;
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