package com.gsmart.services;

import java.util.Map;

import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.GSmartServiceException;

public interface LeaveMasterService {
	
	public Map<String, Object> getLeaveMasterList(Long hid, Integer min, Integer max) throws GSmartServiceException;

	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException;

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException;

	public LeaveMaster editLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException;

}
