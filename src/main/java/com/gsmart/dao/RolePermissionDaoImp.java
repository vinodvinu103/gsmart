package com.gsmart.dao;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Hierarchy;
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

	public Map<String, Object> getPermissionList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		Loggers.loggerStart();
		List<RolePermission> rolePermissions = null;
		Map<String, Object> rolePermissionMap = null;
		Criteria criteria = null;
		getConnection();
		try {
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{
			
			query = session.createQuery("from RolePermission where isActive='Y'");
			}
			 /* else{
				query = session.createQuery("from RolePermission where isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			}
//			rolePermissions = (List<RolePermission>) query.list();
			criteria=session.createCriteria(RolePermission.class);
			criteria.setFirstResult(min);
		     criteria.setMaxResults(max);
		     criteria.setProjection(Projections.id());
		     rolePermissions = criteria.list();
		     Criteria criteriaCount = session.createCriteria(RolePermission.class);
		     criteriaCount.setProjection(Projections.rowCount());
		     Long count = (Long) criteriaCount.uniqueResult();
		     rolePermissionMap.put("totalpermission", query.list().size());
			}*/

			rolePermissions = (List<RolePermission>) query.list();
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd(rolePermissions);
		rolePermissionMap.put("rolePermissions", rolePermissions);
		return rolePermissionMap;
	}

	/**
	 * Adds new permission entity to {@link RolePermission} save it in database
	 * 
	 * @param permission
	 *            instance of RolePermission
	 * @return Nothing
	 */
	@Override
	public RolePermissionCompound addPermission(RolePermission rolePermission) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		RolePermissionCompound cb = null;
		RolePermission permission1 = null;
		try {
			// Hierarchy hierarchy=rolePermission.getHierarchy();
			if (rolePermission.getSubModuleName() == null) {
				permission1 = fetch3(rolePermission);
			} else {
				permission1 = fetch4(rolePermission);
			}

			if (permission1 == null) {
				rolePermission.setEntryTime(CalendarCalculator.getTimeStamp());
				rolePermission.setIsActive("Y");
				cb = (RolePermissionCompound) session.save(rolePermission);
			}
			transaction.commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
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
	 * @return
	 * @return Nothing
	 */

	@Override
	public RolePermission editPermission(RolePermission rolePermission) throws GSmartBaseException {
		getConnection();
		Loggers.loggerStart();
		RolePermission cb = null;

		try {
			RolePermission oldRolePermission = getRolePermission(rolePermission.getEntryTime());
			cb = updateRolePermission(oldRolePermission, rolePermission);
			addPermission(rolePermission);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}

		Loggers.loggerEnd();
		return cb;
	}

	private RolePermission updateRolePermission(RolePermission oldRolePermission, RolePermission rolePermission)
			throws GSmartDatabaseException {
		RolePermission rolePermission1 = null;
		RolePermission ch = null;
		try {
			if (rolePermission.getSubModuleName() == null) {
				if (oldRolePermission.getRole().equals(rolePermission.getRole())
						&& oldRolePermission.getModuleName().equals(rolePermission.getModuleName())) {
					rolePermission1 = fetch(rolePermission);
					if (rolePermission1 == null) {
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						transaction.commit();
						return oldRolePermission;
					}
				} else {
					rolePermission1 = fetch3(rolePermission);
					if (rolePermission1 == null) {
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						transaction.commit();
						return oldRolePermission;
					}

				}

			} else {
				if (oldRolePermission.getRole().equals(rolePermission.getRole())
						&& oldRolePermission.getModuleName().equals(rolePermission.getModuleName())
						&& oldRolePermission.getSubModuleName().equals(rolePermission.getSubModuleName())) {
					rolePermission1 = fetch2(rolePermission);
					if (rolePermission1 == null) {
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						transaction.commit();
						return oldRolePermission;

					}
				} else {

					rolePermission1 = fetch4(rolePermission);
					if (rolePermission1 == null) {
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						transaction.commit();
						return oldRolePermission;
					}

				}

			}
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		return ch;

	}

	private RolePermission fetch3(RolePermission rolePermission) {
		Loggers.loggerStart();
		RolePermission rolePermissionList = null;
		query = session.createQuery("FROM RolePermission WHERE role=:role and moduleName=:moduleName  AND isActive='Y'");
		query.setParameter("role", rolePermission.getRole());

		query.setParameter("moduleName", rolePermission.getModuleName());

		rolePermissionList = (RolePermission) query.uniqueResult();
		Loggers.loggerEnd(rolePermissionList);
		Loggers.loggerEnd();
		return rolePermissionList;
	}

	private RolePermission fetch4(RolePermission rolePermission) {
		Loggers.loggerStart();
		RolePermission rolePermissionList = null;
		query = session.createQuery(
				"FROM RolePermission WHERE role=:role and moduleName=:moduleName and subModuleName=:subModuleName  AND isActive=:isActive");
		query.setParameter("subModuleName", rolePermission.getSubModuleName());
		query.setParameter("role", rolePermission.getRole());
		query.setParameter("moduleName", rolePermission.getModuleName());
		query.setParameter("isActive", "Y");
		rolePermissionList = (RolePermission) query.uniqueResult();
		Loggers.loggerEnd();
		return rolePermissionList;
	}

	private RolePermission fetch2(RolePermission rolePermission) {
		Loggers.loggerStart();
		RolePermission rolePermissionList = null;
		query = session.createQuery(
				"FROM RolePermission WHERE role=:role and moduleName=:moduleName and subModuleName=:subModuleName and add=:add and view=:view and edit=:edit and del=:del  AND isActive=:isActive");
		query.setParameter("subModuleName", rolePermission.getSubModuleName());
		query.setParameter("role", rolePermission.getRole());
		query.setParameter("del", rolePermission.getDel());
		query.setParameter("add", rolePermission.getAdd());
		query.setParameter("view", rolePermission.getView());
		query.setParameter("edit", rolePermission.getEdit());
		query.setParameter("moduleName", rolePermission.getModuleName());
		query.setParameter("isActive", "Y");

		rolePermissionList = (RolePermission) query.uniqueResult();
		Loggers.loggerEnd(rolePermissionList);
		return rolePermissionList;
	}

	public RolePermission fetch(RolePermission rolePermission) {

		Loggers.loggerStart();
		getConnection();
		RolePermission rolePermissionList = null;
		try {

			query = session.createQuery(
					"FROM RolePermission WHERE role=:role moduleName=:moduleName and add=:add and view=:view and edit=:edit and del=:del AND isActive='Y'");
			query.setParameter("role", rolePermission.getRole());
			query.setParameter("del", rolePermission.getDel());
			query.setParameter("add", rolePermission.getAdd());
			query.setParameter("view", rolePermission.getView());
			query.setParameter("edit", rolePermission.getEdit());
			query.setParameter("moduleName", rolePermission.getModuleName());

			rolePermissionList = (RolePermission) query.uniqueResult();
			Loggers.loggerEnd(rolePermissionList);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return rolePermissionList;

	}

	/**
	 * removes the permission entity from the database.
	 * 
	 * @param permission
	 *            instanceOf {@link Permission}
	 * @return Nothing
	 */
	public void deletePermission(RolePermission permission) throws GSmartBaseException {
		getConnection();
		Loggers.loggerStart();

		try {


			permission.setExitTime(CalendarCalculator.getTimeStamp());
			permission.setIsActive("D");
			session.update(permission);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")

	@Override
	public List<RolePermission> getPermission(String role) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();

		List<RolePermission> rolePermissions = new ArrayList<>();

		try {


			Loggers.loggerValue("given role is : ", role);
			query = session.createQuery(
					"SELECT DISTINCT moduleName from RolePermission where role=:role and isActive=:isActive");
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
		} finally {
			session.close();
		}
		Loggers.loggerEnd(rolePermissions);
		return rolePermissions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermission> getSubModuleNames(String role, Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart(role);
		List<RolePermission> rolePermissions = null;
		System.out.println("hierarchry ...." + hierarchy);
		getConnection();
		try {
			query = session.createQuery(
					"from RolePermission where role=:role and moduleName=:moduleName and isActive=:isActive");
			query.setParameter("role", role);
			query.setParameter("isActive", "Y");
			query.setParameter("moduleName", "Maintenance");
			rolePermissions = (List<RolePermission>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd(rolePermissions);
		return rolePermissions;
	}

	/* For Editing */
	public RolePermission getRolePermission(String entryTime) {
		Loggers.loggerStart();
		try {
			query = session.createQuery("from RolePermission where isActive='Y' and entryTime=:entryTime ");
			query.setParameter("entryTime", entryTime);
			RolePermission permission = (RolePermission) query.uniqueResult();
			// session.close();
			Loggers.loggerEnd(permission);
			return permission;
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}

	}

}
