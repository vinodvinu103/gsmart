package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.WeekDays;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class WeekDaysDaoImpl implements WeekDaysDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public List<WeekDays> getWeekList(long hid) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<WeekDays> WeekDaysList = null;
		
		try {
			

			query = session.createQuery("from WeekDays where hid=:hierarchy and isActive='Y'");
			query.setParameter("hierarchy", hid);
			WeekDaysList = query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd();

		return WeekDaysList;

	}

	@Override
	public boolean addWeekDays(WeekDays weekdays) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart(weekdays);
		boolean status=false;
		try {
			query = session.createQuery("from WeekDays where weekDay=:weekDay and hierarchy.hid=:hierarchy and isActive=:isActive");
			query.setParameter("weekDay", weekdays.getWeekDay());
			query.setParameter("isActive", "Y");
			query.setParameter("hierarchy", weekdays.getHierarchy().getHid());
			WeekDays weekDays2=(WeekDays) query.uniqueResult();
			System.out.println("chexh weekday"+weekDays2);
			if(weekDays2==null){
			//WeekDays weekdays1= fetch(weekdays);
			weekdays.setHierarchy(weekdays.getHierarchy());
			weekdays.setEntryTime(CalendarCalculator.getTimeStamp());
			weekdays.setIsActive("Y");
			session.save(weekdays);
			transaction.commit();
			System.out.println("saved successfully");
			status = true;
			}
		} catch (Exception exception) {
			status = false;
			exception.getMessage();
			exception.printStackTrace();
		}finally {
			session.close();
		}
		Loggers.loggerEnd();
		return status;

	}

	@Override
	public void editweekdays(WeekDays weekdays) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		try {
			

			session.save(weekdays);
			transaction.commit();
			session.close();
			WeekDays olddata = getData(weekdays.getWeekDay());
			olddata.setIsActive("N");
			session.update(olddata);
			addWeekDays(weekdays);
		} catch (Exception exception) {

			exception.getMessage();
		}finally {
			session.close();
		}
		Loggers.loggerEnd();

	}

	public WeekDays getData(String days) {
		getConnection();
		Loggers.loggerStart();
		WeekDays day = null;
		try {
			
			query = session.createQuery("from WeekDays where weekDays=:weekDays and isActive='Y'");
			query.setParameter("weekDays", days);

			day = (WeekDays) query.uniqueResult();
			return day;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			session.close();
		}
	}

	@Override
	public void deleteweekdays(WeekDays weekdays) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart(weekdays);
		try {
			
			weekdays.setIsActive("D");
			session.update(weekdays);
			transaction.commit();
			session.close();

		} catch (Exception exception) {

			exception.getMessage();
		}
		Loggers.loggerEnd();
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeekDays> getWeekdaysForHoliday(Long hid) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<WeekDays> days2 = null;
		
		try {
			query = session
					.createQuery("from WeekDays where isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			days2 =query.list();
			return days2;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return days2;
	}

}
