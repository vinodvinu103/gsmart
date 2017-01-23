package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Attendance;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface AttendanceDao  {

	public List<Attendance> getAttendance() throws GSmartDatabaseException;
	
	public Attendance addAttendance(Attendance attendance) throws GSmartDatabaseException;
	
	public void editAttendance(Attendance attedance) throws GSmartDatabaseException;
	
	public void deleteAttendance(Attendance attendance) throws GSmartDatabaseException;

    public List<Attendance> sortAttendance(long startdate ,long enddate)throws GSmartDatabaseException;
    
}