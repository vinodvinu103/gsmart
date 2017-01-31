package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Attendance;
import com.gsmart.model.Holiday;
import com.gsmart.util.GSmartServiceException;

public interface AttendanceService {
 
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId) throws GSmartServiceException;
	
	public Attendance addAttedance(List<Attendance> attendance) throws GSmartServiceException;
	
	public void editAttedance(Attendance attendance) throws GSmartServiceException;
	
    public void deleteAttendance(Attendance attendance) throws GSmartServiceException;
    
}
