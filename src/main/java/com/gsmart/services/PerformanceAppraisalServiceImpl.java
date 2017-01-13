package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.PerformanceAppraisalDao;
import com.gsmart.model.CompoundPerformanceAppraisal;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class PerformanceAppraisalServiceImpl implements PerformanceAppraisalService {

	@Autowired
	PerformanceAppraisalDao appraisalDao;

	
	@Override
	public List<PerformanceAppraisal> getAppraisalList(String reportingId,String year) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return appraisalDao.getAppraisalList(reportingId,year);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
			throw new GSmartServiceException(e.getMessage());

		}
	}

	@Override
	public void addAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException {
		Loggers.loggerStart();

		CompoundPerformanceAppraisal ca = null;

		try {
			 appraisalDao.addAppraisal(appraisal);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}

	@Override
	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			appraisalDao.editAppraisal(appraisal);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {

			throw new GSmartServiceException(e.getMessage());

		}

		Loggers.loggerEnd();
	}

	@Override
	public void deleteAppraisal(PerformanceAppraisal appraisal) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			appraisalDao.deleteAppraisal(appraisal);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerEnd();
	}

	

}
