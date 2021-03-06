package com.gsmart.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Attendance;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;

public interface AttendanceDao  {

	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId) throws GSmartDatabaseException;
	
	public List<String> addAttendance(List<Attendance> attendance) throws GSmartDatabaseException;
	
	public void editAttendance(Attendance attedance) throws GSmartDatabaseException;
	
	public List<Attendance> getAttendanceByhierarchy(Long date, Hierarchy hierarchy);

	public List<Map<String, Object>> getPresentAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException;
	public List<Map<String, Object>> getAbsentAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartDatabaseException;
	public Map<String, Object> getAttendanceCount(List<String> childList,Date date);
	
	public void insertAttendanceData() ;

	public void addClassAttendance(List<Attendance> attendance) throws GSmartDatabaseException;


}