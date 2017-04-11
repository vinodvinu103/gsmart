package com.gsmart.services;

import java.util.List;

import com.gsmart.model.CompoundPerformanceAppraisal;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.GSmartServiceException;

public interface PerformanceAppraisalService {

	public List<PerformanceAppraisal> getAppraisalList(String smartId, String year) throws GSmartServiceException;

	public CompoundPerformanceAppraisal addAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

	public void deleteAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

}
