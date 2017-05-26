package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.model.Roles;
import com.gsmart.util.GSmartServiceException;
/**
 * Provides services for {@link PermissionController}.
 * The functionalities are defined in {@link RolePermissionServicesImpl}
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

public interface RolePermissionServices {
	/**
	 * @return list of RolePermission entities available in the RolePermission Table
	 * @throws GSmartServiceException
	 */

	public Map<String, Object> getPermissionList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartServiceException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public RolePermission editPermission(RolePermission permission) throws GSmartServiceException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deletePermission(RolePermission permission) throws GSmartServiceException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return nothing
	 * @throws GSmartServiceException
	 * 
	 * 
	 */
	
	public List<RolePermission> getPermissionForRole(String role) throws GSmartServiceException;
	
	public boolean addPermissionsForUsers(List<RolePermission> permissionList) throws GSmartServiceException;
	
	
	public RolePermissionCompound addPermission(RolePermission permission) throws GSmartServiceException;

	public Map<String, Object> getPermission(String role) throws GSmartServiceException;
	
	public List<RolePermission> getSubModuleNames(String role) throws GSmartServiceException;
	
	public List<Roles> getRoles()throws GSmartServiceException;
	public List<RolePermission> search(RolePermission permission, Hierarchy hierarchy)throws GSmartServiceException;
	
}
