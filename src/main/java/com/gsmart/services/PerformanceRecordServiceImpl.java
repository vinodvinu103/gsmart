package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.PerformanceRecordDao;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class PerformanceRecordServiceImpl implements PerformanceRecordService {
	@Autowired
	PerformanceRecordDao performancerecordao;
	@Override
	public List<PerformanceRecord> getPerformanceRecord(String smartId,String year) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
	return	performancerecordao.getPerformanceRecord(smartId,year);
		
	
	} catch (GSmartDatabaseException exception) {
		throw (GSmartServiceException) exception;
	} catch (Exception e) {
		Loggers.loggerException(e.getMessage());
		throw new GSmartServiceException(e.getMessage());

	}
	
}
	
	
	
	@Override
	public void addAppraisalRecord(PerformanceRecord appraisal) throws GSmartServiceException {
		try {
		performancerecordao.addAppraisalRecord(appraisal);
		}
		catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}
	@Override
	public void editAppraisalrecord(PerformanceAppraisal appraisal) throws GSmartServiceException {
		try {
			performancerecordao.editAppraisalRecord(appraisal);
			
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}
	@Override
	public void deleteAppraisalrecord(PerformanceAppraisal appraisal) throws GSmartServiceException {
		try {
			
			performancerecordao.deletAppraisalRecord(appraisal);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}
	
}
