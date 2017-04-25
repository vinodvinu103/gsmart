package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;

public interface FeeServices {
	
	public List<Fee> getFeeList(Fee fee,Long hid) throws GSmartServiceException;
	
	public List<Fee> getDashboardFeeList(Fee fee,Long hid) throws GSmartServiceException;
	
	public void addFee(Fee fee) throws GSmartServiceException;
	
	public ArrayList<Fee> getFeeLists(String academicYear,Long hid) throws GSmartServiceException;
	
	public int gettotalfee(String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	public int getPaidFeeDashboard(String academicYear,Long hid, List<String> childList) throws GSmartServiceException;
	
	public int gettotalpaidfee(String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	public int getTotalFeeDashboard(String academicYear,Long hid, List<String> childList) throws GSmartServiceException;

	 public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max) throws GSmartServiceException ;
		
	//public List<Fee> getUnpaidStudentsList(String role,Hierarchy hierarchy) throws GSmartServiceException ;
	
	public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max) throws GSmartServiceException ;

	public void editFee(Fee fee) throws GSmartServiceException;

	public void deleteFee(Fee fee) throws GSmartServiceException;
	
	

	

}
