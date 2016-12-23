package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.GSmartDatabaseException;

public interface LeaveMasterDao {
	public List<LeaveMaster> getLeaveMasterList() throws GSmartDatabaseException;

	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException;

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException;

	public void editLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException;

}