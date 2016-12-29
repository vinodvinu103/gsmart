package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ModulesDao;
import com.gsmart.model.Modules;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
@Service
public class ModulesServicesImpl implements ModulesServices{
	@Autowired
	private ModulesDao modulesDao;

	@Override
	public  List<Modules> getModulesList() throws GSmartServiceException {

		try {
			return modulesDao.getModulesList();

		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}
}
