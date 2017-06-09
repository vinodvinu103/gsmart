package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface LeaveDao {
	
	public Map<String, Object> getLeaveList(Token tokenObj,Hierarchy hierarchy, int min, int max) throws GSmartDatabaseException;
	
	public CompoundLeave addLeave(Leave leave,Integer noOfdays) throws GSmartDatabaseException;

	public void editLeave(Leave leave) throws GSmartDatabaseException;

	public void deleteLeave(Leave leave)throws GSmartDatabaseException;
	
	public List<Leave> getLeaves(String role,Hierarchy hierarchy,String smartId,String leaveType);
	
	public List<Leave> leaveForAdding(Leave leave,Long hid)throws GSmartDatabaseException;

	public List<Leave> searchleave(Leave leave, Long hid)throws GSmartDatabaseException;
}
