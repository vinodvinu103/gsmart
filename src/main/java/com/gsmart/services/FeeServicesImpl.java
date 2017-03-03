package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.FeeDao;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class FeeServicesImpl implements FeeServices {

	@Autowired
	FeeDao feeDao;

	@Override
	public ArrayList<Fee> getFeeList(Fee fee, String role, Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		ArrayList<Fee> feeList = null;
		try {
			feeList = (ArrayList<Fee>) feeDao.getFeeList(fee, role, hierarchy);
			Loggers.loggerStart(feeList);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return feeList;
	}

	@Override
	public void addFee(Fee fee) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			fee.setBalanceFee(fee.getTotalFee() - fee.getPaidFee());
			feeDao.addFee(fee);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		}
		Loggers.loggerEnd();
	}

	/*
	 * @Override public ArrayList<Fee> getFeeLists(String academicYear) throws
	 * GSmartDatabaseException { Loggers.loggerStart(); return
	 * feeDao.getFeeLists(academicYear);
	 * 
	 * }
	 */
	@Override
	public Map<String, Object> getPaidStudentsList(String role, Hierarchy hierarchy, Integer min, Integer max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		Map<String, Object> paidStudentsList = null;
		try {
			paidStudentsList = (Map<String, Object>) feeDao.getPaidStudentsList(role, hierarchy, min, max);
			Loggers.loggerStart(paidStudentsList);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return paidStudentsList;
	}

	@Override
	public Map<String, Object> getUnpaidStudentsList(String role, Hierarchy hierarchy, Integer min, Integer max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		Map<String, Object> unpaidStudentsList = null;
		try {
			unpaidStudentsList = (Map<String, Object>) feeDao.getUnpaidStudentsList(role, hierarchy, min, max);
			Loggers.loggerStart(unpaidStudentsList);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return unpaidStudentsList;
	}

	@Override
	public ArrayList<Fee> getFeeLists(String academicYear, String role, Hierarchy hierarchy)
			throws GSmartServiceException {
		// TODO Auto-generated method stub
		return feeDao.getFeeLists(academicYear, role, hierarchy);
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
			// Loggers.loggerException(e.getMessage());
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
	public int gettotalfee(String role, Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList = new ArrayList<>();
		int totalFees = 0;
		feeList = feeDao.gettotalfee(role, hierarchy);
		for (Fee fee : feeList) {
			totalFees = totalFees + fee.getTotalFee();
		}
		Loggers.loggerEnd();
		return totalFees;
	}

	@Override
	public int gettotalpaidfee(String role, Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> paidList = new ArrayList<>();
		int paidFees = 0;
		paidList = feeDao.gettotalfee(role, hierarchy);
		for (Fee fee : paidList) {
			paidFees = paidFees + fee.getPaidFee();
		}
		Loggers.loggerEnd();
		return paidFees;
	}

	@Override
	public int getPaidFeeDashboard(String academicYear, Hierarchy hierarchy, List<String> childList) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList = new ArrayList<>();
		int paidFees = 0;
		feeList = feeDao.getFeeDashboard(academicYear, hierarchy, childList);
		for (Fee fee : feeList) {
			paidFees += fee.getTotalFee();
		}
		Loggers.loggerEnd();
		return paidFees;
	}

	@Override
	public int getTotalFeeDashboard(String academicYear, Hierarchy hierarchy, List<String> childList)
			throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList = new ArrayList<>();
		int totalFees = 0;
		feeList = feeDao.getFeeDashboard(academicYear, hierarchy, childList);
		for (Fee fee : feeList) {
			totalFees += fee.getPaidFee();
		}
		Loggers.loggerEnd();
		return totalFees;
	}

}
