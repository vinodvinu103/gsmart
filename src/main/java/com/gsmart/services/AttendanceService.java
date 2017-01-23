package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Attendance;
import com.gsmart.util.GSmartServiceException;

public interface AttendanceService {
 
	public List<Attendance> getAttendance() throws GSmartServiceException;
	
	public Attendance addAttedance(Attendance attedance) throws GSmartServiceException;
	
	public void editAttedance(Attendance attendance) throws GSmartServiceException;
	
    public void deleteAttendance(Attendance attendance) throws GSmartServiceException;
    
    public List<Attendance> sortAttendance(long startdate ,long enddate)throws GSmartServiceException;
    
}
