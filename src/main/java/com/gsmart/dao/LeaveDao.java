package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.util.GSmartDatabaseException;

public interface LeaveDao {
	
	public List<Leave> getLeaveList(String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public CompoundLeave addLeave(Leave leave,Integer noOfdays) throws GSmartDatabaseException;

	public void editLeave(Leave leave) throws GSmartDatabaseException;

	public void deleteLeave(Leave leave)throws GSmartDatabaseException;
	
}
