package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundPerformanceAppraisal;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.GSmartServiceException;

public interface PerformanceAppraisalService {

	public List<PerformanceAppraisal> getAppraisalList(String reportingId,String year) throws GSmartServiceException;

	public void addAppraisal(PerformanceAppraisal performanceAppraisal) throws GSmartServiceException;

	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

	public void deleteAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

	public List<PerformanceAppraisal> getTeamAppraisalList(String smartId, String year)throws GSmartServiceException;



}
