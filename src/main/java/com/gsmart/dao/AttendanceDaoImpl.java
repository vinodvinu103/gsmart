package com.gsmart.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Attendance;
import com.gsmart.model.Holiday;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class AttendanceDaoImpl implements AttendanceDao {

	@Autowired
	SessionFactory sessionFactory;
	Session session = null;
	Query query;
	Transaction tx = null;
	DateFormat format = new SimpleDateFormat("");
	Calendar calendar = Calendar.getInstance();

	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Attendance> attendanceList = null;
		try {
			getconnection();
			Loggers.loggerStart(
					"startDate is : " + startDate + " endDate is : " + endDate + " smartId is : " + smartId);
			query = session.createQuery(
					"from Attendance where isActive=:isActive and smartId=:smartId and inDate between :startDate and :endDate");
			query.setParameter("isActive", "Y");
			query.setParameter("smartId", smartId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			attendanceList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return constructAttendanceList((List<Attendance>) attendanceList);
	}

	@Override
	public Attendance addAttendance(List<Attendance> attendanceList) throws GSmartDatabaseException {
		getconnection();
		try {
			for (Attendance attendance : attendanceList) {
				attendance.setInTime(CalendarCalculator.getTimeStamp().toString());
				String date = CalendarCalculator.getTimeStamp();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
				Date date1 = df.parse(date);
				calendar.setTime(date1);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				Date date2 = calendar.getTime();
				long epoch = date2.getTime() / 1000;
				attendance.setInDate(epoch);
				attendance.setIsActive("Y");
				session.save(attendance);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	@Override
	public void editAttendance(Attendance attendance) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getconnection();
			Attendance oldattendance = getAttendance(attendance.getInTime());
			oldattendance.setIsActive("N");
			/* oldattendance.setInTime(CalendarCalculator.getTimeStamp()); */
			attendance.setIsActive("Y");
			attendance.setInTime(CalendarCalculator.getTimeStamp());
			String date = CalendarCalculator.getTimeStamp();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
			Date date1 = df.parse(date);
			long epoch = date1.getTime() / 1000;
			attendance.setInDate(epoch);
			session.saveOrUpdate(attendance);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());

		} finally {
			session.close();
		}
	}

	public Attendance getAttendance(String inTime) {
		Loggers.loggerStart();
		Loggers.loggerStart(inTime);
		try {
			query = session.createQuery("from Attendance where  inTime='" + inTime + "'");
			Attendance attendance = (Attendance) query.uniqueResult();
			return attendance;

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}

	}

	@Override
	public void deleteAttendance(Attendance attendance) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getconnection();
			attendance.setIsActive("D");
			attendance.setOutTime(CalendarCalculator.getTimeStamp());
			session.update(attendance);
			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
	}

	private void getconnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

	}

	private List<Map<String, Object>> constructAttendanceList(List<Attendance> list) {

		List<Map<String, Object>> responseList = new ArrayList();

		for (int i = 0; i < list.size(); i++) {

			Map<String, Object> Map = new HashMap<>();

			Map.put("rfId", list.get(i).getRfId());
			Map.put("InDate", list.get(i).getInDate());
			Map.put("inTime", list.get(i).getInTime());
			Map.put("outTime", list.get(i).getOutTime());
			Map.put("smartId", list.get(i).getSmartId());
			responseList.add(Map);
		}
		return responseList;

	}

	private String convert(Long epoch) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(epoch * 1000);
		return calendar.getTime().toString();
	}

}
