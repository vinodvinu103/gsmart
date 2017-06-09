package com.gsmart.services;

import java.util.List;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.GSmartServiceException;

public interface PerformanceAppraisalService {

	public List<PerformanceAppraisal> getAppraisalList(String reportingId,String year,Long hid) throws GSmartServiceException;

	public void addAppraisal(PerformanceAppraisal performanceAppraisal) throws GSmartServiceException;

	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

	public void deleteAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException;

	public List<PerformanceAppraisal> getTeamAppraisalList(String smartId, String year,Long hid)throws GSmartServiceException;



}
