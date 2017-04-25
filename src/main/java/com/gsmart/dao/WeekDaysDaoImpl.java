package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.WeekDays;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class WeekDaysDaoImpl implements WeekDaysDao {

	@Autowired
	private SessionFactory sessionFactory;

	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public List<WeekDays> getWeekList(long hid) throws GSmartDatabaseException {


		Loggers.loggerStart();
		List<WeekDays> WeekDaysList = null;
		
		try {
			

			query = sessionFactory.getCurrentSession().createQuery("from WeekDays where hid=:hierarchy and isActive='Y'");
			query.setParameter("hierarchy", hid);
			WeekDaysList = query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();

		return WeekDaysList;

	}

	@Override
	public boolean addWeekDays(WeekDays weekdays) throws GSmartDatabaseException {

		Session session=this.sessionFactory.getCurrentSession();
		Loggers.loggerStart(weekdays);
		boolean status=false;
		try {
			query = sessionFactory.getCurrentSession().createQuery("from WeekDays where weekDay=:weekDay and hierarchy.hid=:hierarchy and isActive=:isActive");
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
			System.out.println("saved successfully");
			status = true;
			}
		} catch (Exception exception) {
			status = false;
			exception.getMessage();
			exception.printStackTrace();
		}
		Loggers.loggerEnd();
		return status;

	}

	@Override
	public void editweekdays(WeekDays weekdays) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			
			Session session=this.sessionFactory.getCurrentSession();
			session.save(weekdays);
			WeekDays olddata = getData(weekdays.getWeekDay());
			olddata.setIsActive("N");
			session.update(olddata);
			addWeekDays(weekdays);
		} catch (Exception exception) {

			exception.getMessage();
		}
		Loggers.loggerEnd();

	}

	public WeekDays getData(String days) {
		Loggers.loggerStart();
		WeekDays day = null;
		try {
			
			query = sessionFactory.getCurrentSession().createQuery("from WeekDays where weekDays=:weekDays and isActive='Y'");
			query.setParameter("weekDays", days);

			day = (WeekDays) query.uniqueResult();
			return day;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteweekdays(WeekDays weekdays) throws GSmartDatabaseException {
		Loggers.loggerStart(weekdays);
		try {
			Session session=this.sessionFactory.getCurrentSession();
			
			weekdays.setIsActive("D");
			session.update(weekdays);

		} catch (Exception exception) {

			exception.getMessage();
		}
		Loggers.loggerEnd();
	}

	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<WeekDays> getWeekdaysForHoliday(Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<WeekDays> days2 = null;
		
		try {
			query = sessionFactory.getCurrentSession()
					.createQuery("from WeekDays where isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			days2 =query.list();
			return days2;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return days2;
	}

}
