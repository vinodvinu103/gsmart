package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface FeeDao {
	
	public ArrayList<Fee> getFeeList(Fee fee,Long hid) throws GSmartDatabaseException;
	
	public void addFee(Fee fee) throws GSmartDatabaseException;
	
	public ArrayList<Fee> getFeeLists(String acadamicyear,Long hid) throws GSmartDatabaseException;
	
	public List<Fee> gettotalfee(String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	public List<Fee> getFeeDashboard(String role,Hierarchy hierarchy, List<String> childList) throws GSmartServiceException;

   /* public Map<String, Object> getPaidStudentsList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException;
	*/
	public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max) throws GSmartDatabaseException;

	 public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max) throws GSmartDatabaseException;
		

	
	public void editFee(Fee fee) throws GSmartDatabaseException;

	public void deleteFee(Fee fee) throws GSmartDatabaseException;
}
