package com.gsmart.services;

import java.util.List;

import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
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

	public List<RolePermission> getPermissionList() throws GSmartServiceException;
	/**
	 * @param permission instanceOf {@link RolePermission}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void editPermission(RolePermission permission) throws GSmartServiceException;
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
	 */
	public RolePermissionCompound addPermission(RolePermission permission) throws GSmartServiceException;

	public List<RolePermission> getPermission(String role) throws GSmartServiceException;
	
	public List<RolePermission> getSubModuleNames(String role) throws GSmartServiceException;
	
}
