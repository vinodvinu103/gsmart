package com.gsmart.dao;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

/**
 * provides the implementation for the methods available in
 * {@link PermissionDao} interface
 * 
 * @author :
 * @version 1.0
 * @since 2016-08-01
 */

@Repository
public class RolePermissionDaoImp implements RolePermissionDao {

	@Autowired
	SessionFactory sessionFactory;
	Session session;
	Query query;
	Transaction transaction;

	/**
	 * to view the list of records available in {@link RolePermission} table
	 * 
	 * @return list of permission entities available in RolePermission
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermission> getPermissionList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		List<RolePermission> rolePermissions = null;
		try {
			
			query = session.createQuery("from RolePermission where isActive='Y'");
			rolePermissions = (List<RolePermission>) query.list();

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd(rolePermissions);
		return rolePermissions;
	}

	/**
	 * Adds new permission entity to {@link RolePermission} save it in database
	 * 
	 * @param permission
	 *            instance of RolePermission
	 * @return Nothing
	 */
	@Override
	public RolePermissionCompound addPermission(RolePermission permission) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		RolePermissionCompound cb = null;
		try {
			
			query = session.createQuery(
					"FROM RolePermission where role=:role AND moduleName=:moduleName AND subModuleName=:subModuleName AND isActive=:isActive ");
			query.setParameter("role", permission.getRole());
			query.setParameter("moduleName", permission.getModuleName());
			query.setParameter("subModuleName", permission.getSubModuleName());
			query.setParameter("isActive", "Y");
			RolePermission permission1 = (RolePermission) query.uniqueResult();
			if (permission1 == null) {
				permission.setEntryTime(CalendarCalculator.getTimeStamp());
				permission.setIsActive("Y");
				cb = (RolePermissionCompound) session.save(permission);
			}
			transaction.commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * create instance for session and begins transaction
	 */
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	/**
	 * persists the updated permission instance
	 * 
	 * @param permission
	 *            instance of {@link RolePermission}
	 * @return Nothing
	 */

	@Override
	public void editPermission(RolePermission permission) throws GSmartBaseException {
		Loggers.loggerStart();
		getConnection();
		try {
			
			RolePermission oldRolePermission = getRolePermission(permission.getEntryTime());
			oldRolePermission.setIsActive("N");
			oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldRolePermission);
			permission.setIsActive("Y");
			permission.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(permission);
			session.getTransaction().commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	/**
	 * removes the permission entity from the database.
	 * 
	 * @param permission
	 *            instanceOf {@link Permission}
	 * @return Nothing
	 */
	public void deletePermission(RolePermission permission) throws GSmartBaseException {

		Loggers.loggerStart();
		getConnection();
		try {
			
			permission.setExitTime(CalendarCalculator.getTimeStamp());
			permission.setIsActive("D");
			session.update(permission);
			session.getTransaction().commit();
			session.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")

	@Override
	public List<RolePermission> getPermission(String role) throws GSmartDatabaseException {
		
		Loggers.loggerStart();
		getConnection();
		List<RolePermission> rolePermissions = new ArrayList<>();
		
		try {
			
			Loggers.loggerValue("given role is : ", role);
			query = session.createQuery("SELECT DISTINCT moduleName from RolePermission where role=:role and isActive=:isActive");
			query.setParameter("role", role);
			query.setParameter("isActive", "Y");
			List<String> modules = query.list();
			Loggers.loggerValue("given modules size is : ", modules.size());
			for (String moduleName : modules) {
				RolePermission permission = new RolePermission();
				permission.setModuleName(moduleName);
				Loggers.loggerValue("given moduleName is : ", moduleName);
				rolePermissions.add(permission);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd(rolePermissions);
		return rolePermissions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermission> getSubModuleNames(String role) throws GSmartBaseException {
		Loggers.loggerStart();
		getConnection();
		List<RolePermission> rolePermissions = null;
		try{
			
			query = session.createQuery("from RolePermission where role=:role and moduleName=:moduleName and isActive=:isActive");
			query.setParameter("role", role);
			query.setParameter("isActive", "Y");
			query.setParameter("moduleName", "Maintenance");
			rolePermissions = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}
		return rolePermissions;
	}


	/*For Editing*/
	public RolePermission getRolePermission(String entryTime) {
		Loggers.loggerStart();
		try {
			query = session.createQuery("from RolePermission where isActive='Y' and entryTime=:entryTime");
			query.setParameter("entryTime", entryTime);
			RolePermission permission = (RolePermission) query.uniqueResult();
			return permission;

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}

	}

	
		
		
	
}
