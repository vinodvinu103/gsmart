package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface FeeDao {
	
	public List<Fee> getFeeList(Fee fee,String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public void addFee(Fee fee) throws GSmartDatabaseException;
	
	public ArrayList<Fee> getFeeLists(String smartId) throws GSmartDatabaseException;
	
	public List<Fee> gettotalfee(String role,Hierarchy hierarchy) throws GSmartServiceException;

    public List<Fee> getPaidStudentsList(String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public List<Fee> getUnpaidStudentsList(String role,Hierarchy hierarchy) throws GSmartDatabaseException;

	public void editFee(Fee fee) throws GSmartDatabaseException;

	public void deleteFee(Fee fee) throws GSmartDatabaseException;
}
