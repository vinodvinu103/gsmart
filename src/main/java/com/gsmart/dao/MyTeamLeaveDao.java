package com.gsmart.dao;

import com.gsmart.util.GSmartDatabaseException;

import java.util.List;

import com.gsmart.model.Leave;
import com.gsmart.model.Profile;

public interface MyTeamLeaveDao {
	public List<Leave> getLeavelist(Profile profileInfo,Long hid) throws GSmartDatabaseException;
	public void rejectleave(Leave leave)throws GSmartDatabaseException;
	public void sactionleave(Leave leave)throws GSmartDatabaseException;
	public void cancelSanctionLeave(Leave leave) throws GSmartDatabaseException;

}
