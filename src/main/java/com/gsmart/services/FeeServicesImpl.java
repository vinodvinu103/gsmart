package com.gsmart.services;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.FeeDao;
import com.gsmart.model.Fee;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

@Service
public class FeeServicesImpl implements FeeServices{
	
	@Autowired
	FeeDao feeDao;
	
	Logger logger=Logger.getLogger(FeeServicesImpl.class);
	
	@Override
	public ArrayList<Fee> getFeeList(Fee fee) throws GSmartServiceException {
        logger.debug("Start :: FeeServicesImpl.getFeeList()");
        ArrayList<Fee> feeList = null;
		try{
			feeList=(ArrayList<Fee>) feeDao.getFeeList(fee);
			logger.info(feeList);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		}catch(Exception e){
			throw new GSmartServiceException(e.getMessage());
		}
		 logger.debug("End :: FeeServicesImpl.getFeeList()");
		return feeList;
	}

	@Override
	public void addFee(Fee fee) throws GSmartServiceException {
		 logger.debug("Start :: FeeServicesImpl.addFee()");
		try{
			feeDao.addFee(fee);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		}
		logger.debug("End :: FeeMasterServicesImpl.addFeeMaster()");
	}

	@Override
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartDatabaseException {
		
		return feeDao.getFeeLists(academicYear);

	}

}
