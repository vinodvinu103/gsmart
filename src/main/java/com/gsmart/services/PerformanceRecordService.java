package com.gsmart.services;

import java.util.List;

import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.GSmartServiceException;

public interface PerformanceRecordService {
	
	public List<PerformanceRecord> getPerformanceRecord(PerformanceAppraisal appraisal,String smartId)throws GSmartServiceException;

	public void addAppraisalRecord(PerformanceAppraisal appraisal)throws GSmartServiceException;

	public void editAppraisalrecord(PerformanceAppraisal appraisal)throws GSmartServiceException;

	public void deleteAppraisalrecord(PerformanceAppraisal appraisal)throws GSmartServiceException;


}