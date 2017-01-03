package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Fee;
import com.gsmart.util.GSmartServiceException;

public interface FeeServices {
	
	public List<Fee> getFeeList(Fee fee) throws GSmartServiceException;
	
	public void addFee(Fee fee) throws GSmartServiceException;
	
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartServiceException;

}
