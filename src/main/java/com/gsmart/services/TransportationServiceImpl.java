package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.TransportationDao;
import com.gsmart.model.Transportation;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class TransportationServiceImpl implements TransportationService {

	@Autowired
	TransportationDao TransportationDao;

	@Override
	public List<Transportation> getTransportationFeeList(Long hid) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return TransportationDao.getTransportationfeeList(hid);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}

	}

	@Override
	public Transportation addDetails(Transportation transportation) throws GSmartServiceException {
		 Loggers.loggerStart("entered into addDetails  transportationService ");
		
        Transportation cb = null;
		
		try {
			 Loggers.loggerStart(" transportationService ");
			cb = TransportationDao.addTransportation(transportation);
			 Loggers.loggerStart("data from  TransportationDao");
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	@Override
	public Transportation editTransportationFee(Transportation transportation) throws GSmartServiceException {
		Loggers.loggerStart();
		Transportation cb=null;
		try {
			cb=	TransportationDao.editTransportationFee(transportation);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	
	
	}

	
}
   
	
	