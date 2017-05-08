package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.ModulesDao;
import com.gsmart.model.CompoundModules;
import com.gsmart.model.Hierarchy;

import com.gsmart.model.Modules;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
@Transactional
public class ModulesServicesImpl implements ModulesServices{
	@Autowired
	private ModulesDao modulesDao;

	@Override
	public  List<Modules> getModulesList(String role,Hierarchy hierarchy) throws GSmartServiceException {

		try {
			return modulesDao.getModulesList(role,hierarchy);

		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}
	public CompoundModules addModules(Modules modules) throws GSmartServiceException{
		Loggers.loggerStart();
		
		CompoundModules cb = null;
		
		try {
			cb = modulesDao.addModule(modules);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}
	@Override
	public Modules editmodule(Modules modules) throws GSmartServiceException {
		Loggers.loggerStart();
		Modules ch=null;
		try {
		ch=	modulesDao.editModule(modules);
			
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {

			throw new GSmartServiceException(e.getMessage());

		}

		Loggers.loggerEnd();
		return ch;
	}
	@Override
	public void deletemodule(Modules modules) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			modulesDao.deleteModule(modules);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerEnd();

	}
	
}
