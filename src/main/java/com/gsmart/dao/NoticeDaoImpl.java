package com.gsmart.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Notice;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.Loggers;
import com.gsmart.util.GSmartDatabaseException;


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
	public List<Notice> addNotice(Notice notice) throws GSmartDatabaseException  {
		List<Notice> notices=null;
		try{
			getConnection();
			notice.setIsActive("Y");
			notice.setEntryTime(CalendarCalculator.getTimeStamp()); 
			session.save(notice);
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
		return notices;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> viewAllNotice() throws GSmartDatabaseException {
		try{
			Loggers.loggerStart();
			getConnection();
			query=session.createQuery("from Notice where isActive='Y' AND type='Generic' order by entryTime desc");
			
			List<Notice> notices=query.list();
			//transaction.commit();
			Loggers.loggerStart(notices);
			
			return notices;
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
	}

	@Override
	public void deleteNotice(Notice notice) throws GSmartDatabaseException {
		try{
			getConnection();
			notice= (Notice) session.get("com.gsmart.model.Notice",notice.getEntryTime());
			notice.setIsActive("D");
			notice.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(notice);
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
	}

	@Override
	public List<Notice> editNotice(Notice notice) throws GSmartDatabaseException {
		List<Notice> notices=null;
		try{
			getConnection();
			notice.setIsActive("Y");
			notice.setUpdate_time(CalendarCalculator.getTimeStamp());
			session.update(notice);
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		
		finally {
			session.close();
		}

		return notices;
	}

	@Override
	public List<Notice> viewSpecificNotice(Integer smart_id) throws Exception {
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
/*
	@Override
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
