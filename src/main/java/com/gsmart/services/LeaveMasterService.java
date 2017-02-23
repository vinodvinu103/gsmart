package com.gsmart.services;

import java.util.List;

import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.GSmartServiceException;

public interface LeaveMasterService {
	
	public List<LeaveMaster> getLeaveMasterList(String role,Hierarchy hierarchy) throws GSmartServiceException;

	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException;

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException;

	public LeaveMaster editLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException;

}
