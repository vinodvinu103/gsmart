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

import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Holiday;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;
/**
 * provides the implementation for the methods available in {@link HolidayDao} interface
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01  
 */
@Repository
public class HolidayDaoImpl implements HolidayDao {

	
	@Autowired
	SessionFactory sessionFactory;
	
	Session session = null;
	Transaction transaction = null;
	Query query;

	/**
	 * create instance for session and
	 * begins transaction
	 */
	private void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
	}

	final Logger logger = Logger.getLogger(HolidayDaoImpl.class);
	/**
	 * to view the list of records available in {@link Holiday} table
	 * @return list of holiday entities available in Holiday
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Holiday> getHolidayList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Holiday> holidayList=null;
		try{
			getConnection();
			query = session.createQuery("from Holiday WHERE isActive='Y' ");
			holidayList = query.list();
		}
	 catch (Throwable e) {
		 Loggers.loggerException(e.getMessage());
	}
	finally {

		session.close();
	}
		Loggers.loggerEnd(holidayList);
	return holidayList;
	}
	/**
	 * Adds new holiday entity to {@link Holiday} save it in database
	 * @param holiday instance of Holiday
	 * @return Nothing
	 */
	@Override
	public CompoundHoliday addHoliday(Holiday holiday) throws GSmartDatabaseException {
		CompoundHoliday ch=null;
		Loggers.loggerStart();		
		try {
			getConnection();
			query = session.createQuery("FROM Holiday where holidayDate=:holidayDate and isActive=:isActive");
			query.setParameter("holidayDate", holiday.getHolidayDate());
			query.setParameter("isActive", "Y");
			Holiday holiday1= (Holiday) query.uniqueResult();
			if(holiday1 !=null){
				return null;
			}
			holiday.setEntryTime(CalendarCalculator.getTimeStamp());
			holiday.setIsActive("Y");
			ch=(CompoundHoliday) session.save(holiday);
			
			
			
			transaction.commit();
		} catch (ConstraintViolationException e) {
		throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
			Loggers.loggerEnd();
		
		return ch;
	}
	
    /**
	 * persists the updated holiday instance 
	 * @param holiday instance of {@link Holiday}
	 * @return Nothing
	 */
	@Override
	public void editHoliday(Holiday holiday) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
			Holiday oldholiday= getHolidayList(holiday.getEntryTime());
			oldholiday.setIsActive("N");
			oldholiday.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldholiday);
			
			holiday.setIsActive("Y");
			holiday.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(holiday);
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			Loggers.loggerException(e.getMessage());
			
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		
	}
	public Holiday getHolidayList(String entryTime) {
		try {
			query=session.createQuery("from Holiday where isActive=:isActive and entryTime=:entryTime");
			query.setParameter("isActive", "Y");
			query.setParameter("entryTime", entryTime);
			Holiday holiday = (Holiday) query.uniqueResult();
			return holiday ;
		} 
		catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			return null;
		}
	}
	/**
	 * removes the holiday entity from the database.
	 * @param holiday instanceOf {@link Holiday}
	 * @return Nothing
	 */
	@Override
	public void deleteHoliday(Holiday holiday) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
			holiday.setIsActive("D");
			holiday.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(holiday);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.save(holiday);
		}
		Loggers.loggerEnd();
	}
}
