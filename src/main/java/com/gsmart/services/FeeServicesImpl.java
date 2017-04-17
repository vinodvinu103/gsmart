package com.gsmart.services;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.FeeDao;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class FeeServicesImpl implements FeeServices {

	@Autowired
	private FeeDao feeDao;

	@Override
	public ArrayList<Fee> getFeeList(Fee fee, Long hid) throws GSmartServiceException {
		Loggers.loggerStart();
		ArrayList<Fee> feeList = null;
		try {
			feeList = (ArrayList<Fee>) feeDao.getFeeList(fee,hid);
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
	public ArrayList<Fee> getStudentUnpaidFeeList(Fee fee, Long hid) throws GSmartServiceException {
		Loggers.loggerStart();
		ArrayList<Fee>   StudentUnpaidfeeList = null;
		try {
			StudentUnpaidfeeList = (ArrayList<Fee>) feeDao.getStudentUnpaidFeeList(fee,hid);
		
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return StudentUnpaidfeeList;
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

	@Override
	public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		Map<String, Object> paidStudentsList = null;
		try {
			paidStudentsList = feeDao.getPaidStudentsList(hid, min, max);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd(paidStudentsList);
		return paidStudentsList;
	}

	@Override
	public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		Map<String, Object> unpaidStudentsList = null;
		try {
			unpaidStudentsList = feeDao.getUnpaidStudentsList(hid, min, max);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return unpaidStudentsList;
	}

	@Override
	public ArrayList<Fee> getFeeLists(String academicYear,Long hid) throws GSmartServiceException {
		Loggers.loggerStart(hid);
		return feeDao.getFeeLists(academicYear,hid);
	}

	@Override
	public void editFee(Fee fee) throws GSmartServiceException {
		Loggers.loggerStart(fee);
		try {
		String school=fee.getHierarchy().getSchool();
		String[] str=school.split(" ");
		String schoolname="Fee-";
		for (int i = 0; i < str.length; i++) {
			schoolname=schoolname+str[i].substring(0,1);
		   Loggers.loggerStart(schoolname); 
	
		}
	
			feeDao.editFee(fee,schoolname);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			// Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();

	}
	
	
	
/*	int i=0;
    String year = (Year.now().getValue())+"-"+(Year.now().getValue()+1);

	private String myinvoice(String inst) {
		if (inst=="KAVE") {
			System.out.println("hi suni");
			
		}
		else if(inst.equals("VELL")){
			System.out.println("hi machi");
		}
	Loggers.loggerStart(inst);
		String a="gwr";
		++i;
		String b=a+"-"+year+"-"+i;
		Loggers.loggerValue("b value is ", b);
		return b;
	}*/

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
		List<Fee> feeList = new ArrayList<>();
		int totalFees = 0;
		feeList = feeDao.gettotalfee(role,hierarchy);
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
	public int getPaidFeeDashboard(String academicYear, Long hid, List<String> childList) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList = new ArrayList<>();
		int paidFees = 0;
		feeList = feeDao.getFeeDashboard(academicYear, hid, childList);
		for (Fee fee : feeList) {
			paidFees += fee.getTotalFee();
		}
		Loggers.loggerEnd();
		return paidFees;
	}

	@Override
	public int getTotalFeeDashboard(String academicYear, Long hid, List<String> childList)
			throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> feeList = new ArrayList<>();
		int totalFees = 0;
		feeList = feeDao.getFeeDashboard(academicYear, hid, childList);
		for (Fee fee : feeList) {
			totalFees += fee.getPaidFee();
		}
		Loggers.loggerEnd();
		return totalFees;
	}

	

}
/*------
List<Fee> feeListFromMap = (List<Fee>)fee.get("feeList");
for (int i = 0; i < feeListFromMap.size(); i++) {
	String smartId = feeListFromMap.get(i).getSmartId();
	System.out.println(((Fee) fee.get(i)).getStandard());

}*/
