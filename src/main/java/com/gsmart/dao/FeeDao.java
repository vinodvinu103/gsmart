package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Fee;
import com.gsmart.util.GSmartDatabaseException;

public interface FeeDao {
	
	public List<Fee> getFeeList(Fee fee) throws GSmartDatabaseException;
	
	public void addFee(Fee fee) throws GSmartDatabaseException;
	
	public ArrayList<Fee> getFeeLists(String smartId) throws GSmartDatabaseException;

}
