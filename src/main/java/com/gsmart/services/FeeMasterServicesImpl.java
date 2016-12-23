package com.gsmart.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.FeeMasterDao;
import com.gsmart.model.CompoundFeeMaster;
import com.gsmart.model.FeeMaster;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

/**
 * Provides implementation for services declared in {@link FeeMasterServices}
 * interface. it will go to {@link FeeMasterDao}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

@Service
public class FeeMasterServicesImpl implements FeeMasterServices {

	

	@Autowired
	private FeeMasterDao feeMasterDao;
	

	/**
	 * @return calls {@link FeeMasterDao}'s <code>getFeeList()</code> method
	 */
	@Override
	public List<FeeMaster> getFeeList() throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return feeMasterDao.getFeeList();
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		
		}

	}

	/**
	 * calls {@link FeeMasterDao}'s <code>addFee(...)</code> method
	 * @param feeMaster an instance of {@link FeeMaster} class
	 * @throws GSmartServiceException
	 */
	@Override
	public CompoundFeeMaster addFee(FeeMaster feeMaster) throws GSmartServiceException {
		Loggers.loggerStart();
		
		CompoundFeeMaster cfm = null;
		try {
			cfm = feeMasterDao.addFee(feeMaster);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		
		return cfm;
	}

	/**
	 * calls {@link FeeMasterDao}'s <code>editBand(...)</code> method
	 * @param feeMaster an instance of {@link FeeMaster} class
	 * @throws GSmartServiceException
	 */
	@Override
	public void editFee(FeeMaster feeMaster) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			feeMasterDao.editFee(feeMaster);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

	/**
	 * calls {@link FeeMasterDao}'s <code>deleteFee(...)</code> method
	 * @param timeStamp
	 * @throws GSmartServiceException
	 */
	@Override
	public void deleteFee(FeeMaster feeMaster) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			feeMasterDao.deleteFee(feeMaster);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

	/*@Override
	public void fileUpload(FileUpload fileUpload) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			feeMasterDao.fileUpload(fileUpload);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
	}*/

	/*@Override
	public List getFile(String fileName) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return feeMasterDao.getFile(fileName);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
			
		}
		
		}*/

	
}