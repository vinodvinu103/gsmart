package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.model.Roles;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartDatabaseException;
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
	public RolePermission editPermission(RolePermission permission)throws GSmartBaseException;

	public Map<String, Object> getPermission(String role)throws GSmartBaseException;
	
	public List<RolePermission> getSubModuleNames(String role) throws GSmartDatabaseException;
	
	public List<Roles> getRoles()throws GSmartDatabaseException;
	public List<RolePermission> search(RolePermission permission, Hierarchy hierarchy)throws GSmartDatabaseException;
	public boolean addPermissionsForUsers(List<RolePermission> permissionList) throws GSmartDatabaseException;
	public List<RolePermission> getPermissionForRole(String role) throws GSmartDatabaseException;

	
}
