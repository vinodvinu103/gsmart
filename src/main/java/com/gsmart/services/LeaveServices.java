package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface LeaveServices {
	public Map<String, Object> getLeaveList(String role,Hierarchy hierarchy, int min, int max) throws GSmartServiceException;
	
	public void editLeave(Leave leave) throws GSmartServiceException;
	
	public CompoundLeave addLeave(Leave leave,Integer noOfdays,String role,Hierarchy hierarchy, int min, int max) throws GSmartServiceException;
	
	public void deleteLeave(Leave leave) throws GSmartServiceException;
	
	public Map<String,Object> getLeftLeaves(String role,Hierarchy hierarchy,String smartId,String leaveType);

	Map<String, Object> getLeaveList(Token tokenObj, Hierarchy hierarchy, int min, int max)
			throws GSmartServiceException;

}
