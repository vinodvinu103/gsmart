package com.gsmart.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.AttendanceDao;
import com.gsmart.model.Attendance;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class AttendanceServiceImpl implements AttendanceService {
	
	@Autowired
	AttendanceDao attendancedao;

	@Override
	public List<Attendance> getAttendance() throws GSmartServiceException {
		return attendancedao.getAttendance();
	}

	@Override
	public Attendance addAttedance(Attendance attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		Attendance attend=null;
		try{
			attend=attendancedao.addAttendance(attendance);
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return null;
	}

	@Override
	public void editAttedance(Attendance attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
	         attendancedao.editAttendance(attendance);
		}catch (Exception e) {
          e.printStackTrace();
		}
		Loggers.loggerEnd();
		
	}

	@Override
	public void deleteAttendance(Attendance attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
			attendancedao.deleteAttendance(attendance);
		}catch (Exception e) {
          e.printStackTrace();
		}
		
	}
	@Override
	public List<Attendance> sortAttendance(long startdate, long enddate) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
			attendancedao.sortAttendance(startdate, enddate);
			
		}catch (Exception e) {
          e.printStackTrace();
		}
		return null;
	}

	
}
