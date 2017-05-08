package com.gsmart.services;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.TranspotationFeeDao;
import com.gsmart.model.Fee;
import com.gsmart.model.TransportationFee;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class TranspotationFeeServiceImpl implements TranspotationFeeservice {
	
	@Autowired
	private TranspotationFeeDao transpotationdao;
	

	@Override
	public TransportationFee addTranspotationFee(TransportationFee taranspotationfee) throws GSmartServiceException {
		TransportationFee cb=null;
		try{
		cb=transpotationdao.addTranspotationFee(taranspotationfee);
		 }
		 catch (GSmartDatabaseException exception) {
				throw (GSmartServiceException) exception;
			} catch (Exception e) {
				throw new GSmartServiceException(e.getMessage());
			}
			Loggers.loggerEnd();
			return cb;
	}


	@Override
	public void editTranspotationFee(TransportationFee transpotationfee) throws GSmartServiceException {
		Loggers.loggerStart(transpotationfee);
		try {
		String school=transpotationfee.getHierarchy().getSchool();
		String[] str=school.split(" ");
		String schoolname="Fee-";
		for (int i = 0; i < str.length; i++) {
			schoolname=schoolname+str[i].substring(0,1);
		   Loggers.loggerStart(schoolname); 
	
		}
	
		transpotationdao.editTranspotationFee(transpotationfee,schoolname);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		Loggers.loggerEnd();
		
	}


	@Override
	public void deleteFee(TransportationFee transpotationfee)throws GSmartServiceException {
	
		
	}


	@Override
	public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max) throws GSmartServiceException {
	
		Loggers.loggerStart();
		Map<String, Object> paidStudentsList = null;
		try {
			paidStudentsList = transpotationdao.getPaidStudentsList(hid, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd(paidStudentsList);
		return paidStudentsList;
	}


	@Override
	public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max) throws GSmartServiceException {
	
		Loggers.loggerStart();
		Map<String, Object> unpaidStudentsList = null;
		try {
			unpaidStudentsList = transpotationdao.getUnpaidStudentsList(hid, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return unpaidStudentsList;
	}


	@Override
	public ArrayList<TransportationFee> getTransportationFeeList(TransportationFee transportationFee, Long hid)
			throws GSmartServiceException {
		
		Loggers.loggerStart();
		ArrayList<TransportationFee> feeList = null;
		try {
			feeList = (ArrayList<TransportationFee>) transpotationdao.getTransportationFeeList(transportationFee,hid);
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
	public ArrayList<TransportationFee> getStudentUnpaidFeeList(TransportationFee transportationFee, Long hid)
			throws GSmartServiceException {
		
		Loggers.loggerStart();
		ArrayList<TransportationFee>   StudentUnpaidfeeList = null;
		try {
			StudentUnpaidfeeList = (ArrayList<TransportationFee>) transpotationdao.getStudentUnpaidFeeList(transportationFee,hid);
		
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return StudentUnpaidfeeList;
	}

}
