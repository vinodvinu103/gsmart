/*
* class name: HierarchyDao.java

 *
 * Copyright (c) 2008-2009 Gowdanar Technologies Pvt. Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Gowdanar
 * Technologies Pvt. Ltd.("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Gowdanar Technologie.
 *
 * GOWDANAR TECHNOLOGIES MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GOWDANAR TECHNOLOGIES SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING
 */

package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;

/**
 * 
 * Defines the behavior of all services provided in {@link HierarchysServicesImpl}
 * The functionalities are implemented in {@link HierarchyDaoImpl}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01  
 */
public interface HierarchyDao {

	/**
	 * @return list of Hierarchy entities available in the {@link Hierarchy} Table
	 * @throws Exception
	 */
	public List<Hierarchy> getHierarchyList(String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	/**
	 * @param hierarchy instanceOf {@link Hierarchy}
	 * @return Nothing
	 * @throws Exception
	 */
	public boolean addHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException;
	/**
	 * @param hierarchy instanceOf {@link Hierarchy}
	 * @return Nothing
	 * @throws Exception
	 */
	public Object editHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException;
	/**
	 * @param timeStamp instanceOf {@link Hierarchy}
	 * @return Nothing
	 * @throws Exception
	 */
	public void deleteHierarchy(Hierarchy hierarchy)throws GSmartDatabaseException;
	
	public Hierarchy getHierarchyByHid(Long hid)throws GSmartDatabaseException;


}