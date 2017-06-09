package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Leave;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartServiceException;

public interface MyTeamLeaveServices {
	public Map<String, Object> getLeavelist(Profile profileInfo,Long hierarchy,Integer min,Integer max) throws GSmartServiceException;
	public void rejectleave(Leave leave) throws GSmartServiceException;
	public void sactionleave(Leave leave) throws GSmartServiceException;
	public void cancelSanctionLeave(Leave leave)throws GSmartServiceException;
	public List<Leave> searchmyteamleave(Leave leave, Long hid)throws GSmartServiceException;
}
