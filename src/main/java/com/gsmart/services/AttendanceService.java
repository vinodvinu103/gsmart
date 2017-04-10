package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Attendance;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;

public interface AttendanceService {
 
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId) throws GSmartServiceException;
	
	public List<Map<String, Object>> getPresentAttendance(Long startDate, Long endDate, String smartId) throws GSmartServiceException;
	
	public List<String> addAttedance(List<Attendance> attendance) throws GSmartServiceException;
	
	public void editAttedance(Attendance attendance) throws GSmartServiceException;
	
    public List<Map<String, Object>> getAttendanceByhierarchy(String smartId, Long date, List<Hierarchy> hidList);

	public Map<String, Object> getAttendanceCount(List<String> childList);
}
