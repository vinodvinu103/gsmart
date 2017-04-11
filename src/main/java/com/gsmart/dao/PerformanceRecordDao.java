package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.GSmartDatabaseException;

public interface PerformanceRecordDao {

	public List<PerformanceRecord> getPerformanceRecord(PerformanceAppraisal appraisal,String smartId) throws GSmartDatabaseException;

	public void addAppraisalRecord(PerformanceAppraisal appraisal)throws GSmartDatabaseException;

	public void editAppraisalRecord(PerformanceAppraisal appraisal)throws GSmartDatabaseException;

	public void deletAppraisalRecord(PerformanceAppraisal appraisal)throws GSmartDatabaseException;
		


}
