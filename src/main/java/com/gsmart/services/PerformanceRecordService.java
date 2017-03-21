package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.GSmartServiceException;

public interface PerformanceRecordService {
	
	public Map<String, Object> getPerformanceRecord(String year,String smartId,Long hid,String reportingId)throws GSmartServiceException;

	public void addAppraisalRecord(PerformanceRecord appraisal)throws GSmartServiceException;

	public void editAppraisalrecord(PerformanceAppraisal appraisal)throws GSmartServiceException;

	public void deleteAppraisalrecord(PerformanceAppraisal appraisal)throws GSmartServiceException;

	public Map<String, Object> getPerformanceRecordManager(String reportingManagerId, String smartId, String year,
			Long hid)throws GSmartServiceException;

	public void addAppraisalRecordManager(PerformanceRecord appraisal)throws GSmartServiceException;

	

}
