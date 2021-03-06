package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface LeaveServices {
	public Map<String, Object> getLeaveList(Token tokenObj,Hierarchy hierarchy, int min, int max) throws GSmartServiceException;
	
	public void editLeave(Leave leave) throws GSmartServiceException;
	
	public CompoundLeave addLeave(Leave leave,Integer noOfdays,String smartId,String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	public void deleteLeave(Leave leave) throws GSmartServiceException;
	
	public Map<String,Object> getLeftLeaves(String role,Hierarchy hierarchy,String smartId,String leaveType);

	public List<Leave> searchleave(Leave leave, Long hid)throws GSmartServiceException;

}
