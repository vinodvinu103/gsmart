package com.gsmart.services;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.AttendanceDao;
import com.gsmart.model.Attendance;
import com.gsmart.model.Holiday;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class AttendanceServiceImpl implements AttendanceService {
	
	@Autowired
	AttendanceDao attendancedao;

	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId) throws GSmartServiceException {
		 List<Map<String, Object>> attendenanceList=null;
		try{
			attendenanceList=attendancedao.getAttendance(startDate, endDate, smartId);
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		return attendenanceList;
	}

	@Override
	public Attendance addAttedance(List<Attendance> attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		Attendance attend=null;
		try{
			attend=attendancedao.addAttendance(attendance);
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return attend;
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
	

	
}
