package com.gsmart.dao;

import java.text.DateFormat;
import java.text.ParseException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Attendance;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class AttendanceDaoImpl implements AttendanceDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProfileDao profileDao;

	/*
	 * public void setSessionFactory(SessionFactory sessionFactory) {
	 * this.sessionFactory = sessionFactory; }
	 */
	Query query;
	DateFormat format = new SimpleDateFormat("");
	Calendar calendar = Calendar.getInstance();

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Attendance> attendanceList = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Attendance where isActive=:isActive and smartId=:smartId and inDate between :startDate and :endDate");
			query.setParameter("isActive", "Y");
			query.setParameter("smartId", smartId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			attendanceList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		}

		Loggers.loggerEnd();
		return constructAttendanceList((List<Attendance>) attendanceList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getPresentAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException {

		Loggers.loggerStart();

		List<Attendance> attendanceList = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Attendance where isActive=:isActive and smartId=:smartId and status=:status and inDate between :startDate and :endDate");
			query.setParameter("isActive", "Y");
			query.setParameter("smartId", smartId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("status", "PRESENT");
			attendanceList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		}

		Loggers.loggerEnd();
		return constructAttendanceList((List<Attendance>) attendanceList);
	}
	
	@Override
	public List<Map<String, Object>> getAbsentAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException {
	
		Loggers.loggerStart();

		List<Attendance> attendanceList = null;
		try {
			query =sessionFactory.getCurrentSession().createQuery(
					"from Attendance where isActive=:isActive and smartId=:smartId and status=:status and inDate between :startDate and :endDate");
			query.setParameter("isActive", "Y");
			query.setParameter("smartId", smartId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("status", "ABSENT");
			attendanceList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		}

		Loggers.loggerEnd();
		return constructAttendanceList((List<Attendance>) attendanceList);
	}

	@Override
	public List<String> addAttendance(List<Attendance> attendanceList) throws GSmartDatabaseException {

		List<String> rfidList = new ArrayList<>();
		Loggers.loggerStart("attendanceList : " + attendanceList + " " + attendanceList.size());
		try {
			Session session = this.sessionFactory.getCurrentSession();
			for (Attendance attendance : attendanceList) {
				Profile profile = getSmartId(attendance.getRfId());
				if (profile.getSmartId() != null) {
					attendance.setSmartId(profile.getSmartId());
					attendance.setHierarchy(profile.getHierarchy());
					session.update(attendance);
					rfidList.add(attendance.getRfId());
				}
			}
			System.out.println("atttenfdenceListjj" + attendanceList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		}
		Loggers.loggerEnd(rfidList);
		return rfidList;
	}

	@Override
	public void editAttendance(Attendance attendance) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session = this.sessionFactory.getCurrentSession();

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
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			throw new GSmartDatabaseException(e.getMessage());

		}
	}

	public Attendance getAttendance(long inDate, String smartId) {
		Loggers.loggerStart(inDate);
		try {
			query = sessionFactory.getCurrentSession()
					.createQuery("from Attendance where  inDate=:inDate and smartId=:smartId");
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

	/*
	 * private void getconnection() { session = sessionFactory.openSession(); tx
	 * = session.beginTransaction();
	 * 
	 * }
	 */

	private List<Map<String, Object>> constructAttendanceList(List<Attendance> list) {

		List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Attendance> getAttendanceByhierarchy(Long date, Hierarchy hierarchy) {
		Loggers.loggerStart();
		System.out.println("intime >>>>>>>>>>>>>>>>>>>>>>>>>> " + date);
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Attendance where inDate=:inDate and hierarchy.hid=:hId");
			query.setParameter("inDate", date);
			query.setParameter("hId", hierarchy.getHid());
			return query.list();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}
	}

	private Profile getSmartId(String rfid) {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Profile where rfid=:rfid");
			query.setParameter("rfid", rfid);
			return (Profile) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}
	}

	@Override
	public Map<String, Object> getAttendanceCount(List<String> childList) {
		Loggers.loggerStart();
		try {
			Map<String, Object> respMap = new HashMap<>();
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
			System.out.println("today epoch date" + epoch1);
			System.out.println("childList" + childList);

			query = sessionFactory.getCurrentSession().createQuery(
					"from Attendance where smartId in  (:smartIdList) and isActive=:isActive and inDate=:inDate");
			query.setParameterList("smartIdList", childList);
			query.setParameter("isActive", "Y");
			query.setParameter("inDate", epoch1);

			respMap.put("Attendancecount", query.list().size());
			respMap.put("childList", query.list());
			respMap.put("totalCount", childList.size());

			return respMap;
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}
	}

	@Scheduled(cron = "0 0 1 * * ?")
	public void insertAttendanceData() {
		
		String date = CalendarCalculator.getTimeStamp();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
		Date date1;
		int year = Calendar.getInstance().get(Calendar.YEAR);   // Gets the current date and time
		
		String academicYear=year+"-"+(year+1);
		try {
			date1 = df.parse(date);
			calendar.setTime(date1);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			Date date2 = calendar.getTime();
			long epoch1 = date2.getTime() / 1000;
			System.out.println("today epoch date" + epoch1);
			System.out.println("attendance insert data using cron job");
			ArrayList<Profile> allProfiles = profileDao.getAllProfiles(academicYear);
			
			
			for (Profile profile : allProfiles) {
				String smartId=profile.getSmartId();
//				String rfid=profile.getRfId();
				if (profile.getSmartId() != null && profile.getRfId()!=null) {
					Attendance attendance = new Attendance();
					Session session = this.sessionFactory.getCurrentSession();
					attendance.setSmartId(smartId);
					attendance.setRfId(smartId);
					attendance.setStatus("ABSENT");
					attendance.setIsActive("N");
					attendance.setInDate(epoch1);
					attendance.setHierarchy(profile.getHierarchy());
					session.save(attendance);
					System.out.println("data saved in attendance table" + attendance);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();

		}
	}
}
