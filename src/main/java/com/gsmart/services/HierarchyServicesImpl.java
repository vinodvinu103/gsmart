package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.HierarchyDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

/**
 * Provides implementation for services declared in {@link HierarchyServices}
 * interface. it will go to {@link HierarchyDao}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Service
public class HierarchyServicesImpl implements HierarchyServices {

	@Autowired
	HierarchyDao hierarchyDao;

	/**
	 * @return calls {@link HierarchyDao}'s <code>getHierarchyList()</code>
	 *         method
	 */
	@Override
	public List<Hierarchy> getHierarchyList() throws GSmartServiceException {
		return hierarchyDao.getHierarchyList();
	}

	/**
	 * calls {@link HierarchyDao}'s <code>addHierarchy(...)</code> method
	 * 
	 * @param hierarchy
	 *            an instance of {@link Hierarchy} class
	 * @throws GSmartServiceException
	 */

	@Override
	public boolean addHierarchy(Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		boolean status;
		try {
			status = hierarchyDao.addHierarchy(hierarchy);
		} catch (Exception e) {
			status = false;
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return status;
	}

	/**
	 * calls {@link HierarchyDao}'s <code>editHierarchy(...)</code> method
	 * 
	 * @param hierarchy
	 *            an instance of {@link Hierarchy} class
	 * @throws GSmartServiceException
	 */
	@Override
	public Hierarchy editHierarchy(Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		Hierarchy ch = null;
		try {
			ch = (Hierarchy) hierarchyDao.editHierarchy(hierarchy);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return ch;
	}

	/**
	 * calls {@link HierarchyDao}'s <code>deleteHierarchy(...)</code> method
	 * 
	 * @param hierarchy
	 *            an instance of {@link Hierarchy} class
	 * @throws GSmartServiceException
	 */
	@Override
	public void deleteHierarchy(Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		hierarchyDao.deleteHierarchy(hierarchy);
		Loggers.loggerEnd();
	}

	@Override
	public Hierarchy getHierarchyByHid(Long hid) throws GSmartServiceException {
		return getHierarchyByHid(hid);
	}

	@Override
	public List<Hierarchy> getAllHierarchy() {
		// TODO Auto-generated method stub
		return hierarchyDao.getAllHierarchy();
	}

}