package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
import com.gsmart.util.GSmartServiceException;

public interface FeeServices {
	
	public List<Fee> getFeeList(Fee fee) throws GSmartServiceException;
	
	public void addFee(Fee fee) throws GSmartServiceException;
	
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartServiceException;

    public List<Fee> getPaidStudentsList() throws GSmartServiceException ;
		
	public List<Fee> getUnpaidStudentsList() throws GSmartServiceException ;

	public void editFee(Fee fee) throws GSmartServiceException;

	public void deleteFee(Fee fee) throws GSmartServiceException;
	
	

	

}
