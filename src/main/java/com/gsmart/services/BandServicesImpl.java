package com.gsmart.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.BandDao;
import com.gsmart.model.Band;
import com.gsmart.model.CompoundBand;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

/**
 * Provides implementation for services declared in {@link BandServices}
 * interface. it will go to {@link BandDao}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-02-23
 */
@Service
public class BandServicesImpl implements BandServices {

	

	@Autowired
	private BandDao bandDao;

	/**
	 * @return calls {@link BandDao}'s <code>getBandList()</code> method
	 */
	@Override
	public Map<String, Object> getBandList(int min, int max) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return bandDao.getBandList(min, max);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}

	}
	
	@Override
	public List<Band> getBandList1() throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return bandDao.getBandList1();
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}

	}

	/**
	 * calls {@link BandDao}'s <code>addBand(...)</code> method
	 * 
	 * @param band
	 *            an instance of {@link Band} class
	 * @throws GSmartServiceException
	 */
	@Override
	public CompoundBand addBand(Band band) throws GSmartServiceException {
		Loggers.loggerStart();
		
		CompoundBand cb = null;
		
		try {
			cb = bandDao.addBand(band);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * calls {@link BandDao}'s <code>editBand(...)</code> method
	 * 
	 * @param band
	 *            an instance of {@link Band} class
	 * @throws GSmartServiceException
	 */
	@Override
	public Band editBand(Band band) throws GSmartServiceException {
		Loggers.loggerStart();
		Band cb=null;
		try {
			cb=	bandDao.editBand(band);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * calls {@link BandDao}'s <code>deleteBand(...)</code> method
	 * 
	 * @param band
	 *            an instance of {@link Band} class
	 * @throws Exception
	 */
	@Override
	public void deleteBand(Band band) throws GSmartServiceException {
		
		Loggers.loggerStart();
		try {
			bandDao.deleteBand(band);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}
	}
