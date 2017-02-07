package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Attendance;
import com.gsmart.model.Holiday;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface AttendanceDao  {

	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId) throws GSmartDatabaseException;
	
	public Attendance addAttendance(List<Attendance> attendance) throws GSmartDatabaseException;
	
	public void editAttendance(Attendance attedance) throws GSmartDatabaseException;
	


}