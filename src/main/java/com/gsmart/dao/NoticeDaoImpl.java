package com.gsmart.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Notice;
import com.gsmart.util.CalendarCalculator;

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
	public void addNotice(Notice notice){
		try{
			getConnection();
			notice.setIs_active("Y");
			notice.setEntry_time(CalendarCalculator.getTimeStamp()); 
			session.save(notice);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			session.close();
		}

	}

	@Override
	public List<Notice> viewNotice() {
		try{
			getConnection();
			query=session.createQuery("from Notice where is_active='Y' ");
			//query.setMaxResults(6);
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

	@Override
	public void deleteNotice(Notice notice) {
		try{
			getConnection();
			notice= (Notice) session.get("com.gsmart.model.Notice",notice.getEntry_time());
			notice.setIs_active("D");
			notice.setExit_time(CalendarCalculator.getTimeStamp());
			session.update(notice);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			session.close();
		}
	}

	@Override
	public void editNotice(Notice notice){
		try{
			getConnection();
			notice.setIs_active("Y");
			notice.setUpdate_time(CalendarCalculator.getTimeStamp());
			session.update(notice);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			session.close();
		}

	}

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
}
