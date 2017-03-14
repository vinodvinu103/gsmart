package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.GSmartDatabaseException;

public interface LeaveMasterDao {
	public Map<String, Object> getLeaveMasterList(Long hid, Integer min, Integer max) throws GSmartDatabaseException;

	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException;

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException;

	public LeaveMaster editLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException;
	
	public LeaveMaster getLeaveMasterByType(String role,Hierarchy hierarchy,String leaveType);

	public List<LeaveMaster> getLeaveMasterListForApplyLeave(Long hid) throws GSmartDatabaseException;
}
