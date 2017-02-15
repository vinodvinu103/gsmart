package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.FeeDao;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class FeeServicesImpl implements FeeServices{
	
	@Autowired
	FeeDao feeDao;
	
	
	
	@Override
	public ArrayList<Fee> getFeeList(Fee fee,String role,Hierarchy hierarchy) throws GSmartServiceException {
        Loggers.loggerStart();
        ArrayList<Fee> feeList = null;
		try{
			feeList=(ArrayList<Fee>) feeDao.getFeeList(fee,role,hierarchy);
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

	/*@Override
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartDatabaseException {
		Loggers.loggerStart();
		return feeDao.getFeeLists(academicYear);

	}
*/
	@Override
	public List<Fee> getPaidStudentsList(String role,Hierarchy hierarchy) throws GSmartServiceException {
		 Loggers.loggerStart();
	        List<Fee> paidStudentsList = null;
			try{
				paidStudentsList=(List<Fee>) feeDao.getPaidStudentsList(role,hierarchy);
				Loggers.loggerStart(paidStudentsList);
			}catch (GSmartDatabaseException exception) {
				throw (GSmartServiceException) exception;
			}catch(Exception e){
				throw new GSmartServiceException(e.getMessage());
			}
			 Loggers.loggerEnd();
			return paidStudentsList;
	}

	@Override
	public List<Fee> getUnpaidStudentsList(String role,Hierarchy hierarchy) throws GSmartServiceException {
		 Loggers.loggerStart();
	        List<Fee> unpaidStudentsList = null;
			try{
				unpaidStudentsList=(List<Fee>) feeDao.getUnpaidStudentsList(role,hierarchy);
				Loggers.loggerStart(unpaidStudentsList);
			}catch (GSmartDatabaseException exception) {
				throw (GSmartServiceException) exception;
			}catch(Exception e){
				throw new GSmartServiceException(e.getMessage());
			}
			 Loggers.loggerEnd();
			return unpaidStudentsList;
	}

	@Override
	public ArrayList<Fee> getFeeLists(String academicYear,String role,Hierarchy hierarchy) throws GSmartServiceException {
		// TODO Auto-generated method stub
		return feeDao.getFeeLists(academicYear,role,hierarchy);
	}

	@Override
	public void editFee(Fee fee) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			feeDao.editFee(fee);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			//Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}

	@Override
	public void deleteFee(Fee fee) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			feeDao.deleteFee(fee);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}

	

	@Override
	public int gettotalfee(String role,Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList=null;
		int totalFees=0;
		feeList=feeDao.gettotalfee(role,hierarchy);
		for(Fee fee : feeList)
		{
			totalFees=totalFees+fee.getTotalFee();	
		}
		Loggers.loggerEnd();
		return  totalFees;
	}

	@Override
	public int gettotalpaidfee(String role,Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> paidList=null;
		int paidFees=0;
		paidList=feeDao.gettotalfee(role,hierarchy);
		for(Fee fee : paidList)
		{
			paidFees=paidFees+fee.getPaidFee();	
		}
		Loggers.loggerEnd();
		return  paidFees;
	}

}
