package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartServiceException;

public interface MyTeamLeaveServices {
	public List<Leave> getLeavelist(Profile profileInfo,Hierarchy hierarchy) throws GSmartServiceException;
	public void rejectleave(Leave leave) throws GSmartServiceException;
	public void sactionleave(Leave leave) throws GSmartServiceException;
	public void cancelSanctionLeave(Leave leave)throws GSmartServiceException;
}
