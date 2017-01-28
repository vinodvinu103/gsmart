package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.AttenanceDao;

import com.gsmart.model.Attenance;
import com.gsmart.model.Band;
import com.gsmart.model.CompoundAttenance;
import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Inventory;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class AttenanceServiceImpl implements AttenanceServices {
	
	

	@Autowired
	private AttenanceDao attenanceDao;
	
	
	@Override
	public List<Attenance> getAttenanceList() throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			Loggers.loggerEnd();
			return attenanceDao.getAttenanceList();
		} catch (GSmartDatabaseException Exception ) {
			throw(GSmartServiceException) Exception;
			
		}catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		
		
	}
	
	
	
	
	@Override
	public void editAttenance(Attenance attenance)throws GSmartServiceException{
	
	
	
	
		Loggers.loggerStart();
		try {
			attenanceDao.editAttenance(attenance);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}


	@Override
	public void deleteAttenance(Attenance attenance)throws GSmartServiceException  {
		
		Loggers.loggerStart();
		try {
			attenanceDao.deleteAttenance(attenance);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}




	@Override
	public CompoundAttenance addAttenace(Attenance attenance) throws GSmartServiceException {
		Loggers.loggerStart();
		CompoundAttenance cb;
		try {
			cb=attenanceDao.addAttenance(attenance);
			
		}catch (GSmartDatabaseException exception) {
			
			throw (GSmartServiceException) exception;
		} 
		catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}
	}


