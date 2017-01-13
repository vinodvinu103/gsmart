package com.gsmart.dao;

import java.util.List;
import com.gsmart.model.CompoundPerformanceAppraisal;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.GSmartDatabaseException;

public interface PerformanceAppraisalDao {

	public List<PerformanceAppraisal> getAppraisalList(String reportingId,String year) throws GSmartDatabaseException;

	public void addAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException;

	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException;

	public void deleteAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException;

}
