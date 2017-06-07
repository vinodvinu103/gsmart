package com.gsmart.dao;

import com.gsmart.util.GSmartDatabaseException;

import java.util.Map;

import com.gsmart.model.Leave;
import com.gsmart.model.Profile;

public interface MyTeamLeaveDao {
	public Map<String, Object> getLeavelist(Profile profileInfo,Long hierarchy,Integer min,Integer max) throws GSmartDatabaseException;
	public void rejectleave(Leave leave)throws GSmartDatabaseException;
	public void sactionleave(Leave leave)throws GSmartDatabaseException;
	public void cancelSanctionLeave(Leave leave) throws GSmartDatabaseException;
	public List<Leave> searchmyteamleave(Leave leave, Long hid)throws GSmartDatabaseException;

}
