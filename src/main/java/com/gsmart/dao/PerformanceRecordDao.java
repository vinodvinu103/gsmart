package com.gsmart.dao;

import java.util.Map;

import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.GSmartDatabaseException;

public interface PerformanceRecordDao {

	public Map<String, Object> getPerformanceRecord(String smartId,String year,Long hid,String reportingId) throws GSmartDatabaseException;

	public void addAppraisalRecord(PerformanceRecord appraisal)throws GSmartDatabaseException;

	public void editAppraisalRecord(PerformanceAppraisal appraisal)throws GSmartDatabaseException;

	public void deletAppraisalRecord(PerformanceAppraisal appraisal)throws GSmartDatabaseException;

	public Map<String, Object> getPerformanceRecordManager(String reportingManagerId, String smartId, String year,
			Long hid)throws GSmartDatabaseException;

	public void addAppraisalRecordManager(PerformanceRecord appraisal)throws GSmartDatabaseException;

	public Map<String, Object> getrating(String year, Long hid)throws GSmartDatabaseException;

	
		


}
