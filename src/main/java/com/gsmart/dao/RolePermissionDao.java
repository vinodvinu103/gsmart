package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.util.GSmartBaseException;
/**
 * 
 * Defines the behavior of all services provided in {@link RolePermissionServicesImpl}
 * The functionalities are implemented in {@link RolePermissionDaoImpl}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01  
 */

public interface RolePermissionDao {
	/**
	 * @return list of RolePermission entities available in the {@link RolePermission} Table
	 * @throws GSmartDatabaseException
	 */

	public Map<String, Object> getPermissionList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartBaseException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */
	public RolePermissionCompound addPermission(RolePermission permission) throws GSmartBaseException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */
	public void deletePermission(RolePermission permission) throws GSmartBaseException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */
	public void editPermission(RolePermission permission)throws GSmartBaseException;

	public List<RolePermission> getPermission(String role)throws GSmartBaseException;
	
	public List<RolePermission> getSubModuleNames(String role,Hierarchy hierarchy) throws GSmartBaseException;

	
}
