package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Attendance;
import com.gsmart.model.Fee;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface DashboardDao {

	public List<Attendance> getAttendance();

	/*public int getInventory();

	public int getTotalfee();*/
	
	public List<ReportCard> examName(Token tokenDetail,String smartId,String academicYear)throws GSmartDatabaseException;
	
	public List<Fee> academicYear(Token tokenObj,String smartId)throws GSmartDatabaseException;
}
