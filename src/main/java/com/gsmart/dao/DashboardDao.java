package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Attendance;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface DashboardDao {

	public List<Attendance> getAttendance();

	/*public int getInventory();

	public int getTotalfee();*/
	
	public List<ReportCard> examName(Token tokenDetail,String smartId,String academicYear)throws GSmartDatabaseException;
	
	public List<Fee> academicYear(Token tokenObj,String smartId)throws GSmartDatabaseException;
	
	public Map<String, Object> getInventoryAssignList(String role, String smartId, Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException;

	public List<Profile> searchStudentByName(Profile profile, Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public List<Profile> searchStudentById(Profile profile, Hierarchy hierarchy) throws GSmartDatabaseException;

	public List<Profile> studentProfile(Token tokenObj, Hierarchy hierarchy);
}
