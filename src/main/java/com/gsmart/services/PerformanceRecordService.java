package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.GSmartServiceException;

public interface PerformanceRecordService {
	
	public List<PerformanceRecord> getPerformanceRecord(String year,String smartId,String role,Hierarchy hierarchy)throws GSmartServiceException;

	public void addAppraisalRecord(PerformanceRecord appraisal)throws GSmartServiceException;

	public void editAppraisalrecord(PerformanceAppraisal appraisal)throws GSmartServiceException;

	public void deleteAppraisalrecord(PerformanceAppraisal appraisal)throws GSmartServiceException;

	

}
