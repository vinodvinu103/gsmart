package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.TimeTableDao;
import com.gsmart.model.CompoundTimeTable;
import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class TimeTableDoaImpl implements TimeTableDao {

	@Autowired
	private SessionFactory sessionFactory;
	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeTable> teacherView(String day, String academicYear, Token token) throws GSmartDatabaseException {
		Loggers.loggerStart(day);
		List<TimeTable> teacherList = new ArrayList<>();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			query = session.createQuery(
					"from TimeTable where isActive='Y' and smartId=:smartId and day=:day and academicYear=:academicYear and hid=:hierarchy order by time");
			query.setParameter("smartId", token.getSmartId());
			query.setParameter("day", day);
			query.setParameter("academicYear", academicYear);
			query.setParameter("hierarchy", token.getHierarchy().getHid());
			teacherList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(teacherList);
		return teacherList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeTable> studentView(String day, String academicYear, Token token, String standard, String section)
			throws GSmartDatabaseException {
		Loggers.loggerStart(day);
		Loggers.loggerStart(academicYear);
		Loggers.loggerStart(section);
		Loggers.loggerStart(standard);
		Loggers.loggerStart(token);
		List<TimeTable> teacherList = new ArrayList<>();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			query = session.createQuery(
					"from TimeTable where isActive='Y' and day=:day and standard=:standard and section=:section and academicYear=:academicYear and "
							+ "hid=:hierarchy order by period");
			query.setParameter("day", day);
			query.setParameter("standard", standard);
			query.setParameter("section", section);
			query.setParameter("academicYear", academicYear);
			query.setParameter("hierarchy", token.getHierarchy().getHid());
			//query.setParameter("role", token.getRole());
			teacherList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Loggers.loggerEnd(teacherList);
		return teacherList;
	}

	@Override
	public CompoundTimeTable addTTable(TimeTable timeTable, Token tokenObjt) throws GSmartDatabaseException {
		Loggers.loggerStart();
		CompoundTimeTable table2 = null;
		TimeTable table = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			query = sessionFactory.getCurrentSession().createQuery(
					"from TimeTable where isActive='Y' and time=:time and day=:day and standard=:standard and teacherName=:teacherName and section=:section and "
							+ "hid=:hierarchy and role=:role");
			query.setParameter("time", timeTable.getTime());
			query.setParameter("day", timeTable.getDay());
			query.setParameter("standard", timeTable.getStandard());
			query.setParameter("section", timeTable.getSection());
			query.setParameter("teacherName", timeTable.getTeacherName()	);
			query.setParameter("hierarchy", tokenObjt.getHierarchy().getHid());
			query.setParameter("role", timeTable.getRole());
			table = (TimeTable) query.uniqueResult();
			if (table == null) {
				System.out.println("<<<<<<  in side add method >>>>>>>>");
				timeTable.setEntryTime(CalendarCalculator.getTimeStamp());
				timeTable.setIsActive("Y");
				timeTable.setHierarchy(tokenObjt.getHierarchy());
				table2 = (CompoundTimeTable) session.save(timeTable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return table2;
	}
	
	@Override
	public TimeTable editTimeTable(TimeTable timeTable,Token tokenObjt) throws GSmartDatabaseException {
		Loggers.loggerStart(timeTable);
		TimeTable table=null;
		try {
			TimeTable oldTT=getTT(timeTable.getEntryTime());
			table = updateTT(oldTT, timeTable);
			if (table != null){
				
				if(addTTable(timeTable, tokenObjt)!=null){
					return table;
				}
				else
					table=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
	
	@Override
	public void deleteTimeTable(TimeTable timeTable) throws GSmartDatabaseException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Loggers.loggerStart();

			timeTable.setExitTime(CalendarCalculator.getTimeStamp());
			timeTable.setIsActive("D");
			session.update(timeTable);
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TimeTable getTT(String entryTime){
		Loggers.loggerStart();
		TimeTable table=null;
		try {
			query = sessionFactory.getCurrentSession()
					.createQuery("from TimeTable where entryTime=:entryTime and isActive=:isActive");
			query.setParameter("entryTime", entryTime);
			query.setParameter("isActive", "Y");
			table = (TimeTable) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return table;
	}
	
	public TimeTable updateTT(TimeTable oldTT, TimeTable newTT) {
		Loggers.loggerStart();
		Session session = this.sessionFactory.getCurrentSession();
		TimeTable tt2 = null;
		try {
			TimeTable tt3 = fetch(newTT);
			if (tt3 == null) {
				oldTT.setUpdateTime(CalendarCalculator.getTimeStamp());
				oldTT.setIsActive("N");
				session.update(oldTT);

				return oldTT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tt2;
	}
	
	private TimeTable fetch(TimeTable newTT) throws GSmartDatabaseException {
		Loggers.loggerStart();

		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from TimeTable where isActive='Y' and smartId=:smartId and day=:day and "
					+ "academicYear=:academicYear and hid=:hierarchy and subject=:subject and "
					+ "standard=:standard and section=:section and time=:time");
			query.setParameter("smartId", newTT.getSmartId());
			query.setParameter("day", newTT.getDay());
			query.setParameter("academicYear", newTT.getAcademicYear());
			query.setParameter("hierarchy", newTT.getHierarchy().getHid());
			query.setParameter("subject", newTT.getSubject());
			query.setParameter("standard", newTT.getStandard());
			query.setParameter("section", newTT.getSection());
			query.setParameter("time", newTT.getTime());
			TimeTable list = (TimeTable) query.uniqueResult();

			return list;
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeTable> hodViewForTeacher(String day, String academicYear, Token tokenObj, String smartId) {
		Loggers.loggerStart(day);
		List<TimeTable> teacherList = new ArrayList<>();
		try {
			Session session = this.sessionFactory.getCurrentSession();
			query = session.createQuery(
					"from TimeTable where isActive='Y' and smartId=:smartId and day=:day and academicYear=:academicYear and hid=:hierarchy "
					+ "and role=:role order by time");
			query.setParameter("smartId", smartId);
			query.setParameter("day", day);
			query.setParameter("academicYear", academicYear);
			query.setParameter("role", "TEACHER");
			query.setParameter("hierarchy", tokenObj.getHierarchy().getHid());
			teacherList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(teacherList);
		return teacherList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TimeTable> getChildTeacher(TimeTable timeTable, Token tokenObj) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<TimeTable> list=null;
		try {
			Session session = this.sessionFactory.getCurrentSession();
			query=session.createQuery("from TimeTable where isActive='Y' and day=:day "
					+ "and time=:time and hid=:hierarchy");
			query.setParameter("time", timeTable.getTime());
			query.setParameter("day", timeTable.getDay());
			query.setParameter("hierarchy", tokenObj.getHierarchy().getHid());
			list=query.list();
			Loggers.loggerEnd(list);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}


}
