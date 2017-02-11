package com.gsmart.dao;

import java.util.Calendar;
import java.util.Date;
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
import com.gsmart.model.Hierarchy;
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
	public List<Holiday> getHolidayList(String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		List<Holiday> holidayList=null;
		try{
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{
			query = session.createQuery("from Holiday WHERE isActive='Y' ");
			}else{
				query = session.createQuery("from Holiday WHERE isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
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
		getConnection();
		CompoundHoliday ch=null;
		Loggers.loggerStart();
		
		try {
			
			
           Calendar calendar=Calendar.getInstance();
			
			calendar.setTime(holiday.getHolidayDate());
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			Date holidayDate=calendar.getTime();
			Holiday holiday1=fetch(holiday);
			if(holiday1 !=null){
				System.out.println("not equal null");
				return null;
			}else{
				System.out.println("null");
			
			holiday.setHolidayDate(holidayDate);
			holiday.setEntryTime(CalendarCalculator.getTimeStamp());
			holiday.setIsActive("Y");
			ch=(CompoundHoliday) 
			session.save(holiday);
			transaction.commit();
			}
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
	public Holiday editHoliday(Holiday holiday) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		Holiday ch=null;
		try {
			
			Holiday oldholiday= getHolidayLists(holiday.getEntryTime(),holiday.getHierarchy());
			ch=updateHoliday(oldholiday, holiday);
			addHoliday(holiday);
			
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			Loggers.loggerException(e.getMessage());
			
		}
		Loggers.loggerEnd();
		return ch;
		/*finally {
			session.close();
		}*/
		
		
	}
private Holiday updateHoliday(Holiday oldholiday, Holiday holiday) throws GSmartDatabaseException {
		
	Holiday ch = null;
		try {
			Holiday holiday1 = fetch(holiday);
			if (holiday1 == null) {
				oldholiday.setUpdatedTime(CalendarCalculator.getTimeStamp());
				oldholiday.setIsActive("N");
				session.update(oldholiday);

				
				transaction.commit();
				return oldholiday;

			}
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		return ch;

	}
	public Holiday getHolidayLists(String entryTime,Hierarchy hierarchy) {
		try {
			query=session.createQuery("from Holiday where isActive=:isActive and entryTime=:entryTime and hierarchy.hid=:hierarchy");
			query.setParameter("isActive", "Y");
			query.setParameter("entryTime", entryTime);
			query.setParameter("hierarchy", hierarchy.getHid());
			Holiday holiday = (Holiday) query.uniqueResult();
			return holiday ;
		} 
		catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			return null;
		}
	}
	
	public Holiday fetch(Holiday holiday) {
		
		Holiday holidayList=null;
		try {
Calendar calendar=Calendar.getInstance();
			
			calendar.setTime(holiday.getHolidayDate());
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			Date holidayDate=calendar.getTime();
			Hierarchy hierarchy=holiday.getHierarchy();
			query = session.createQuery("FROM Holiday where holidayDate=:holidayDate and isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("holidayDate", holidayDate);
			query.setParameter("isActive", "Y");
		holidayList= (Holiday) query.uniqueResult();
		}catch (Exception e) {

			e.printStackTrace();
		}
			return holidayList; 

		}
	/**
	 * removes the holiday entity from the database.
	 * @param holiday instanceOf {@link Holiday}
	 * @return Nothing
	 */
	@Override
	public void deleteHoliday(Holiday holiday) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		try {
			
			holiday.setIsActive("D");
			holiday.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(holiday);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}
}
