

package com.gsmart.services;

import java.util.List;

import com.gsmart.controller.HierarchyController;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;

/**
 * Provides services for {@link HierarchyController}.
 * The functionalities are defined in {@link HierarchyServicesImpl}
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

public interface HierarchyServices {
	/**
	 * @return list of Hierarchy entities available in the Hierarchy Table
	 * @throws GSmartServiceException
	 */	
	public List<Hierarchy> getHierarchyList() throws GSmartServiceException;
	/**
	 * @param hierarchy instanceOf {@link Hierarchy}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public boolean addHierarchy(Hierarchy hierarchy) throws GSmartServiceException;
	/**
	 * @param hierarchy instanceOf {@link Hierarchy}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
     public Hierarchy editHierarchy(Hierarchy hierarchy) throws GSmartServiceException;
	/**
	 * @param hierarchy instanceOf {@link Hierarchy}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deleteHierarchy(Hierarchy hierarchy)throws GSmartServiceException;
	
	public Hierarchy getHierarchyByHid(Long hid)throws GSmartServiceException;
	
	
	
}