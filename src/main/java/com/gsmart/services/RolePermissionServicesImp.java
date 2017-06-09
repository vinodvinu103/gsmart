package com.gsmart.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.RolePermissionDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.model.Roles;
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
@Transactional
public class RolePermissionServicesImp implements RolePermissionServices {
	
	@Autowired
	private RolePermissionDao rolePermissionDao;
	
	/**
	 * @return calls {@link PermissionDao}'s <code>getPermissionList()</code> method
	 */
	@Override
	public Map<String, Object> getPermissionList(String role,Hierarchy hierarchy, Integer min, Integer max)  throws GSmartServiceException{ 
		
		Loggers.loggerStart();
		try {
			Loggers.loggerEnd();
			return rolePermissionDao.getPermissionList(role,hierarchy, min, max);
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
	public Map<String, Object> getPermission(String role) throws GSmartServiceException {

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
	public List<RolePermission> getSubModuleNames(String role,String moduleName) throws GSmartServiceException {

		List<RolePermission> list = null;
		
		try{
			list = rolePermissionDao.getSubModuleNames(role,moduleName);
			
			for (RolePermission rolePermission : list) {
				
				switch(rolePermission.getSubModuleName()){
				case "BAND":
					rolePermission.setIcon("white fa fa-bitcoin fa-3x");
					break;
				case "HOLIDAY":
					rolePermission.setIcon("white fa fa-calendar fa-3x");
					break;
				case "HIERARCHY":
					rolePermission.setIcon("white fa fa-gg fa-3x");
					break;
				case "PRIVILEGE":
					rolePermission.setIcon("white fa fa-bar-chart fa-3x");
					break;
				case "INVENTORY":
					rolePermission.setIcon("white fa fa-home fa-3x");
					break;
				case "LEAVEMASTER":
					rolePermission.setIcon("white fa fa-th-list fa-3x");
					break;
				case "FEEMASTER":
					rolePermission.setIcon("white fa fa-cc-visa fa-3x");
					break;
				case "ROLEPERMISSION":
					rolePermission.setIcon("white fa fa-unlock-alt fa-3x");
					break;
				case "RFID":
					rolePermission.setIcon("white fa fa-home fa-3x");
					break;					
				case "ASSIGN":
					rolePermission.setIcon("white fa fa-th-list fa-3x");
					break;	
				case "GRADES":
					rolePermission.setIcon("white fa fa-percent fa-3x");
					break;
				case "BANNER":
					rolePermission.setIcon("white fa fa-picture-o fa-3x");
					break;
				case "TRANSPORTATIONFEE":
					rolePermission.setIcon("white fa fa-bus fa-3x");
					break;
				default :
					rolePermission.setIcon("white fa fa-home fa-3x");
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
	@Override
	public List<Roles> getRoles() throws GSmartServiceException {
		Loggers.loggerStart();
		List<Roles> roles=null;
		try {
			roles=rolePermissionDao.getRoles();
			
		} catch (GSmartDatabaseException exception ) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		
		
		Loggers.loggerEnd();
		return roles;
	}
	@Override
	public List<RolePermission> search(RolePermission permission, Hierarchy hierarchy) throws GSmartServiceException {
		return rolePermissionDao.search(permission, hierarchy);
	}
	@Override
	public boolean addPermissionsForUsers(List<RolePermission> permissionList)
			throws GSmartServiceException {
		Loggers.loggerStart();
		boolean status=false;
		try {
			status=rolePermissionDao.addPermissionsForUsers(permissionList);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return status;
	}
	@Override
	public List<RolePermission> getPermissionForRole(String role) throws GSmartServiceException {
		Loggers.loggerStart();
		List<RolePermission> rolePermission=null;
		try {
			rolePermission=rolePermissionDao.getPermissionForRole(role);
			
		} catch (GSmartDatabaseException exception ) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		
		
		Loggers.loggerEnd();
		return rolePermission;
	}	
	

	}