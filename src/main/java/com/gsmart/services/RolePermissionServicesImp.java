package com.gsmart.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsmart.dao.RolePermissionDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
/**
 * Provides implementation for services declared in {@link PermissionServices}
 * interface. it will go to {@link permissionDao}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Service
public class RolePermissionServicesImp implements RolePermissionServices {
	
	@Autowired
	RolePermissionDao rolePermissionDao;
	
	/**
	 * @return calls {@link PermissionDao}'s <code>getPermissionList()</code> method
	 */
	@Override
	public List<RolePermission> getPermissionList(String role,Hierarchy hierarchy)  throws GSmartServiceException{ 
		
		Loggers.loggerStart();
		try {
			Loggers.loggerEnd();
			return rolePermissionDao.getPermissionList(role,hierarchy);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

	}
	/**
	 * calls {@link PermissionDao}'s <code>addPermission(...)</code> method
	 * 
	 * @param permission
	 *            an instance of {@link RolePermission} class
	 * @throws GSmartServiceException
	 */
	@Override
	public RolePermissionCompound addPermission(RolePermission permission) throws GSmartServiceException {

		Loggers.loggerStart();
		RolePermissionCompound cb = null;
		try {
			cb=rolePermissionDao.addPermission(permission);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	

	}
	/**
	 * calls {@link PermissionDao}'s <code>editPermission(...)</code> method
	 * 
	 * @param permission
	 *            an instance of {@link RolePermission} class
	 * @throws GSmartServiceException
	 */
	@Override
	public RolePermission editPermission(RolePermission permission) throws GSmartServiceException{
		Loggers.loggerStart();
		RolePermission cb = null;
		try {
			cb	= rolePermissionDao.editPermission(permission);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * calls {@link PermissionDao}'s <code>deletePermission(...)</code> method
	 * 
	 * @param permission
	 *            an instance of {@link RolePermission} class
	 * @throws GSmartServiceException
	 */
	public void deletePermission(RolePermission permission) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			 rolePermissionDao.deletePermission(permission);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

	@Override
	public List<RolePermission> getPermission(String role) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			Loggers.loggerEnd();
			return rolePermissionDao.getPermission(role);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}	
	}
	
	@Override
	public List<RolePermission> getSubModuleNames(String role,Hierarchy hierarchy) throws GSmartServiceException {

		List<RolePermission> list = null;
		
		try{
			list = rolePermissionDao.getSubModuleNames(role,hierarchy);
			
			for (RolePermission rolePermission : list) {
				
				switch(rolePermission.getSubModuleName()){
				case "Band":
					rolePermission.setIcon("white fa fa-bitcoin fa-3x");
					break;
				case "Holiday":
					rolePermission.setIcon("white fa fa-calendar fa-3x");
					break;
				case "Hierarchy":
					rolePermission.setIcon("white fa fa-gg fa-3x");
					break;
				case "Privilege":
					rolePermission.setIcon("white fa fa-bar-chart fa-3x");
					break;
				case "Inventory":
					rolePermission.setIcon("white fa fa-home fa-3x");
					break;
				case "LeaveMaster":
					rolePermission.setIcon("white fa fa-th-list fa-3x");
					break;
				case "FeeMaster":
					rolePermission.setIcon("white fa fa-cc-visa fa-3x");
					break;
				case "RolePermission":
					rolePermission.setIcon("white fa fa-unlock-alt fa-3x");
					break;
				case "RFID":
					rolePermission.setIcon("white fa fa-home fa-3x");
					break;					
				case "Assign":
					rolePermission.setIcon("white fa fa-th-list fa-3x");
					break;	
				}
				
			}
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		
		return list;
	}	
	

	}