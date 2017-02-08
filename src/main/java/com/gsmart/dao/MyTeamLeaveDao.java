package com.gsmart.dao;

import com.gsmart.util.GSmartDatabaseException;

import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;

public interface MyTeamLeaveDao {
	public List<Leave> getLeavelist(String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	public void rejectleave(Leave leave)throws GSmartDatabaseException;
	public void sactionleave(Leave leave)throws GSmartDatabaseException;
	public void cancelSanctionLeave(Leave leave) throws GSmartDatabaseException;

}
