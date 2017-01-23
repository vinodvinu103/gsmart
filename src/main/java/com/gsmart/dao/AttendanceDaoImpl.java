package com.gsmart.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;
@Repository
public class AttendanceDaoImpl implements AttendanceDao {
	
	@Autowired
	SessionFactory sessionFactory;
	Session session=null;
	Query query;
	Transaction tx=null;
	

	@Override
	public List<Attendance> getAttendance() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Attendance> attendanceList=null;
		try{
			getconnection();
			query = session.createQuery("from Attendance where isActive=:isActive");
			query.setParameter("isActive","Y");
		     attendanceList=query.list();
      }catch (Exception e) {
        e.printStackTrace();
      }
		Loggers.loggerEnd();
		return attendanceList;
	}
	
	@Override
	public List<Attendance> sortAttendance(long startdate, long enddate)throws GSmartDatabaseException{
		getconnection();
		Date attendanceList=null;
		List<Attendance> list=null;
		try{
			Loggers.loggerValue("sortattndance","");
			query = session.createQuery("from Attendance where inDate between " + startdate + " and " + enddate +"");
			list=query.list();
			/*for(int i=0;i<list.size();i++){
				String  epochString=(list.get(i).getInTime());
			    SimpleDateFormat df =new SimpleDateFormat("MM dd yyyy");
			    Date date =df.parse(epochString);
			    long epoch = Long.parseLong( epochString );
			    attendanceList = new Date( epoch * 1000 );
			}*/
		    
			Loggers.loggerValue("attndanceList",list);

		  }catch (Exception e) {
            e.printStackTrace();
		  }
		System.out.println((List<Attendance>) attendanceList);
		return (List<Attendance>) attendanceList;
	
	}

	@Override
	public Attendance addAttendance(Attendance attendance) throws GSmartDatabaseException {
		getconnection();
		Attendance attend=null;
		try{
	        	attendance.setInTime(CalendarCalculator.getTimeStamp());
	        	String date=CalendarCalculator.getTimeStamp();
	            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
	            Date date1 = df.parse(date);
	            long epoch = date1.getTime()/1000;
	            attendance.setInDate(epoch);
	            session.save(attendance);
	            tx.commit();
		}catch (Exception e) {
          e.printStackTrace();
          
          }
		
		
		return attendance;
	}

	@Override
	public void editAttendance(Attendance attedance) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try{
		getconnection();
		Attendance oldattendance=getAttendance(attedance.getInTime());
		oldattendance.setIsActive("N");
		oldattendance.setInTime(CalendarCalculator.getTimeStamp());
		session.save(attedance);
		session.getTransaction().commit();
		}catch (Exception e) {
         e.printStackTrace();
		Loggers.loggerException(e.getMessage());

		}finally {
			session.close();
		}
	}
	
	public Attendance getAttendance(String inTime) {
		Loggers.loggerStart();
		try {
			query = session.createQuery("from Assign where isActive='Y' and inTime=:inTime'" + inTime + "'");
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
		}finally {
			session.close();
		}
	}

	private void getconnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
	}

	
	
	private List<Map<String,Object>> ConstructAttend(List<Attendance> list,String Convert){
		
		List<Map<String,Object>> List =query.list();
		
		for(int i=0;i<list.size(); i++){
			
			Map<String, Object> Map=new HashMap<>();
			
			Map.put("rfId", list.get(i).getRfId());
			Map.put("InDate", Convert(list.get(i).getInDate()));
		    Map.put("inTime", list.get(i).getInTime());
			Map.put("outTime", list.get(i).getOutTime());
			Map.put("smartId", list.get(i).getSmartId());
			list.add((Attendance) Map);
		}
		return List;
		
	}
	 
	private String Convert(Long epoch){
		
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String epoch1 = format.format(epoch);	
		
		return epoch1;

	}
	
	
}
