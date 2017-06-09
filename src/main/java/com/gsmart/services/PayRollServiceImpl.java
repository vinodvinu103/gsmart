package com.gsmart.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.PayRollDao;
import com.gsmart.model.PayRoll;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class PayRollServiceImpl implements PayRollService {

	@Autowired
	private PayRollDao payrolldao;
	
	
	@Override
	public Map<String, Object> getPayroll() throws GSmartServiceException {
		/*Loggers.loggerStart();
		try {*/
		return payrolldao.getPayroll();
		/*}catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}*/
	}

	@Override
	public PayRoll addPayroll(PayRoll payroll) throws GSmartServiceException {
		Loggers.loggerStart();
		PayRoll cb=null;
		try {
	      cb=payrolldao.addPayroll(payroll);
		}catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	@Override
	public PayRoll editPayroll(PayRoll payroll) throws GSmartServiceException{
		Loggers.loggerStart();
		PayRoll cb=null;
		try {
		cb=payrolldao.editPayroll(payroll);
		}catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
		
	}

	@Override
	public void deletePayroll(PayRoll payroll) throws GSmartServiceException{
		
		Loggers.loggerStart();
		try {
			payrolldao.deletePayroll(payroll);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}
}
