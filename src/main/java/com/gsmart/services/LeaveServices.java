package com.gsmart.services;

import java.util.List;

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Leave;
import com.gsmart.util.GSmartServiceException;

public interface LeaveServices {
	public List<Leave> getLeaveList() throws GSmartServiceException;
	
	public void editLeave(Leave leave) throws GSmartServiceException;
	
	public CompoundLeave addLeave(Leave leave,Integer noOfdays) throws GSmartServiceException;
	
	public void deleteLeave(Leave leave) throws GSmartServiceException;

}