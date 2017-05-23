package com.gsmart.dao;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Holiday;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

/**
 * provides the implementation for the methods available in {@link HolidayDao}
 * interface
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Repository
@Transactional
public class HolidayDaoImpl implements HolidayDao {
	@Autowired
	private SessionFactory sessionFactory;

	/* public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}*/

	Query query;

	/**
	 * create instance for session and begins transaction
	 */
	/*private void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}
*/
	final Logger logger = Logger.getLogger(HolidayDaoImpl.class);

	/**
	 * to view the list of records available in {@link Holiday} table
	 * 
	 * @return list of holiday entities available in Holiday
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getHolidayList(Long hid,int min,int max )throws GSmartDatabaseException {

		Loggers.loggerStart();
		List<Holiday> holidayList = null;
		Map<String, Object> holidayMap = new HashMap<>();
		Criteria criteria = null;
		try {
			

			criteria = sessionFactory.getCurrentSession().createCriteria(Holiday.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			criteria.addOrder(Order.asc("holidayDate"));
			holidayList = criteria.list();
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Holiday.class);
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			System.out.println("count"+count);
			holidayMap.put("totalholidaylist", count);
		} catch (Throwable e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} 
		holidayMap.put("holidayList", holidayList);
		Loggers.loggerEnd(holidayList);

		return holidayMap;
	}

	/**
	 * Adds new holiday entity to {@link Holiday} save it in database
	 * 
	 * @param holiday
	 *            instance of Holiday
	 * @return Nothing
	 */
	@Override
	public CompoundHoliday addHoliday(Holiday holiday) throws GSmartDatabaseException {
		CompoundHoliday ch = null;
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			
			Holiday holiday1=fetch(holiday);
			if(holiday1==null){
			holiday.setHolidayDate(getTimeWithOutMillis(holiday.getHolidayDate()));
			holiday.setEntryTime(CalendarCalculator.getTimeStamp());
			holiday.setIsActive("Y");
			ch = (CompoundHoliday) session.save(holiday);
			session.save(holiday);
			}
		}catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();

		return ch;
	}

	/**
	 * persists the updated holiday instance
	 * 
	 * @param holiday
	 *            instance of {@link Holiday}
	 * @return 
	 */
	@Override
	public Holiday editHoliday(Holiday holiday) throws GSmartDatabaseException {
		Loggers.loggerStart();
		
		Holiday ch=null;
		try {
			Holiday oldholiday = getHolidayLists(holiday.getEntryTime(), holiday.getHierarchy());

			ch=updateHoliday(oldholiday, holiday);
			addHoliday(holiday);
			
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			Loggers.loggerException(e.getMessage());

		} 
		Loggers.loggerEnd();
		
		return ch;
		
		
	}
	private Date getTimeWithOutMillis(Date date)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date holidayDate=calendar.getTime();
		return holidayDate;
	}
private Holiday updateHoliday(Holiday oldholiday, Holiday holiday) throws GSmartDatabaseException {
	Holiday holiday1 =null;
	Session session=this.sessionFactory.getCurrentSession();
	Holiday ch = null;
		try {
			if(getTimeWithOutMillis(oldholiday.getHolidayDate()).equals(getTimeWithOutMillis(holiday.getHolidayDate()))){
					oldholiday.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldholiday.setIsActive("N");
					session.update(oldholiday);

					
					return oldholiday;
			
			}else{
				holiday1=fetch(holiday);
				if (holiday1 == null) {
					oldholiday.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldholiday.setIsActive("N");
					session.update(oldholiday);

					
					return oldholiday;
				
			}
			

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

				query=sessionFactory.getCurrentSession().createQuery("from Holiday where isActive=:isActive and entryTime=:entryTime and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			
			query.setParameter("isActive", "Y");
			query.setParameter("entryTime", entryTime);
			
			
			Holiday holiday = (Holiday) query.uniqueResult();
			return holiday;
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			return null;
		}
	}
	public Holiday fetch(Holiday holiday) {
		Loggers.loggerStart();
		Holiday holidayList=null;
		try {
				
				query = sessionFactory.getCurrentSession().createQuery("FROM Holiday where holidayDate=:holidayDate and isActive=:isActive and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", holiday.getHierarchy().getHid());
			
			query.setParameter("holidayDate", getTimeWithOutMillis(holiday.getHolidayDate()));
			query.setParameter("isActive", "Y");
			
			
		holidayList= (Holiday) query.uniqueResult();
		}catch (Exception e) {

			e.printStackTrace();
		}
		Loggers.loggerEnd();
			return holidayList; 

		}
	/**
	 * removes the holiday entity from the database.
	 * 
	 * @param holiday
	 *            instanceOf {@link Holiday}
	 * @return Nothing
	 */
	@Override
	public void deleteHoliday(Holiday holiday) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		
		try {

			holiday.setIsActive("D");
			holiday.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(holiday);

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Holiday> holidayList(Long hid) throws GSmartDatabaseException {
		try {
			Session session=this.sessionFactory.getCurrentSession();
			Criteria criteria = null;
			criteria = session.createCriteria(Holiday.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			
			
			return criteria.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		
	}

	@Override
	public List<Holiday> searchHoliday(Holiday holiday, Long hid) throws GSmartDatabaseException {
		List<Holiday> searchHoliday = null;
		try{
			if(hid!=null){
			query = sessionFactory.getCurrentSession().createQuery("from Holiday where description like '%"+ holiday.getDescription() +"%' and isActive = 'Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			}else{
				query = sessionFactory.getCurrentSession().createQuery("from Holiday where description like '%"+ holiday.getDescription() +"%' and isActive = 'Y'");
			}
			searchHoliday = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return searchHoliday;
	}
}
