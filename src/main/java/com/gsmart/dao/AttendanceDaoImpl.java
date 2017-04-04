package com.gsmart.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();

		List<Attendance> attendanceList = null;
		try {
			query = session.createQuery(
					"from Attendance where isActive=:isActive and smartId=:smartId and inDate between :startDate and :endDate");
			query.setParameter("isActive", "Y");
			query.setParameter("smartId", smartId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			attendanceList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}

		Loggers.loggerEnd();
		return constructAttendanceList((List<Attendance>) attendanceList);
	}

	@Override
	public List<String> addAttendance(List<Attendance> attendanceList) throws GSmartDatabaseException {
		
		List<String> rfidList = new ArrayList<>();
		Loggers.loggerStart("attendanceList : " + attendanceList + " " + attendanceList.size());
		try {
			for (Attendance attendance : attendanceList) {
				Profile profile = getSmartId(attendance.getRfId());
				getconnection();
				if(profile.getSmartId() != null) {
					attendance.setSmartId(profile.getSmartId());
					attendance.setHierarchy(profile.getHierarchy());
					session.saveOrUpdate(attendance);
					tx.commit();
					rfidList.add(attendance.getRfId());
				}
			}
			System.out.println("atttenfdenceListjj"+attendanceList);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
		Loggers.loggerEnd(rfidList);
		return rfidList;
	}

	@Override
	public void editAttendance(Attendance attendance) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();

		try {
			Attendance oldattendance = getAttendance(attendance.getInDate(), attendance.getSmartId());
			if (oldattendance != null) {
				if (oldattendance.getIsActive().equalsIgnoreCase("n")) {
					oldattendance.setIsActive("Y");
					session.update(oldattendance);
				} else {
					oldattendance.setIsActive("N");
					session.update(oldattendance);
				}
			} else {
				attendance.setIsActive("Y");
				attendance.setInTime(Calendar.getInstance().getTimeInMillis() / 1000);
				attendance.setOutTime(Calendar.getInstance().getTimeInMillis() / 1000);
				String date = CalendarCalculator.getTimeStamp();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
				Date date1 = df
						.parse(date); /* long epoch = date1.getTime() / 1000; */
				calendar.setTime(date1);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				Date date2 = calendar.getTime();
				long epoch1 = date2.getTime() / 1000;
				attendance.setInDate(epoch1);
				session.save(attendance);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
	}

	public Attendance getAttendance(long inDate, String smartId) {
		Loggers.loggerStart(inDate);
		try {
			query = session.createQuery("from Attendance where  inDate=:inDate and smartId=:smartId");
			query.setParameter("smartId", smartId);
			query.setParameter("inDate", inDate);
			Attendance attendance = (Attendance) query.uniqueResult();
			return attendance;

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
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

	@Override
	public List<Attendance> getAttendanceByhierarchy(Long date, Hierarchy hierarchy) {
		Loggers.loggerStart();
		System.out.println("intime >>>>>>>>>>>>>>>>>>>>>>>>>> "+date);
		getconnection();
		try {
			query = session.createQuery("from Attendance where inDate=:inDate and hierarchy.hid=:hId");
			query.setParameter("inDate", date);
			query.setParameter("hId", hierarchy.getHid());
			return query.list();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		} finally {
			session.close();
		}
	}
	
	private Profile getSmartId(String rfid) {
		Loggers.loggerStart();
		getconnection();
		try {
			query = session.createQuery("from Profile where rfid=:rfid");
			query.setParameter("rfid", rfid);
			return (Profile) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		} finally {
			session.close();
		}
	}
}
