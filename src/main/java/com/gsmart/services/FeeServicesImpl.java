package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.FeeDao;
import com.gsmart.model.Fee;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class FeeServicesImpl implements FeeServices{
	
	@Autowired
	FeeDao feeDao;
	
	
	
	@Override
	public ArrayList<Fee> getFeeList(Fee fee) throws GSmartServiceException {
        Loggers.loggerStart();
        ArrayList<Fee> feeList = null;
		try{
			feeList=(ArrayList<Fee>) feeDao.getFeeList(fee);
			Loggers.loggerStart(feeList);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		}catch(Exception e){
			throw new GSmartServiceException(e.getMessage());
		}
		 Loggers.loggerEnd();
		return feeList;
	}

	@Override
	public void addFee(Fee fee) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
			fee.setBalanceFee(fee.getTotalFee()-fee.getPaidFee());
			feeDao.addFee(fee);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		}
		Loggers.loggerEnd();
	}

	@Override
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartDatabaseException {
		Loggers.loggerStart();
		return feeDao.getFeeLists(academicYear);

	}

	@Override
	public int gettotalfee() throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList=null;
		int totalFees=0;
		feeList=feeDao.gettotalfee();
		for(Fee fee : feeList)
		{
			totalFees=totalFees+fee.getTotalFee();	
		}
		Loggers.loggerEnd();
		return  totalFees;
	}

	@Override
	public int gettotalpaidfee() throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> paidList=null;
		int paidFees=0;
		paidList=feeDao.gettotalfee();
		for(Fee fee : paidList)
		{
			paidFees=paidFees+fee.getPaidFee();	
		}
		Loggers.loggerEnd();
		return  paidFees;
	}

}
