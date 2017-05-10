package com.gsmart.dao;

import java.security.acl.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.model.Roles;
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
@Transactional
public class RolePermissionDaoImp implements RolePermissionDao {

	@Autowired
	private SessionFactory sessionFactory;
	Query query;

	/**
	 * to view the list of records available in {@link RolePermission} table
	 * 
	 * @return list of permission entities available in RolePermission
	 */
	@SuppressWarnings("unchecked")
	@Override

	public Map<String, Object> getPermissionList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Loggers.loggerStart();
		List<RolePermission> rolePermissions = null;
		Map<String, Object> rolePermissionMap = new HashMap<>();
		Criteria criteria = null;
		
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(RolePermission.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.setFirstResult(min);
		     criteria.setMaxResults(max);
		     criteria.addOrder(Order.asc("role"));
		     rolePermissions = criteria.list();		     
		     Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(RolePermission.class);
		     criteriaCount.add(Restrictions.eq("isActive", "Y"));
		     criteriaCount.setProjection(Projections.rowCount());
		     rolePermissionMap.put("totalpermission", criteriaCount.uniqueResult());
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
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
		Session session=this.sessionFactory.getCurrentSession();
		RolePermissionCompound cb = null;
		RolePermission permission1 = null;
		try {
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
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * create instance for session and begins transaction
	 */
	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

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
		Session session=this.sessionFactory.getCurrentSession();
		RolePermission rolePermission1 = null;
		RolePermission ch = null;
		try {
			if (rolePermission.getSubModuleName() == null) {
				if (oldRolePermission.getRole().equals(rolePermission.getRole())
						&& oldRolePermission.getModuleName().equals(rolePermission.getModuleName())) {
					
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						return oldRolePermission;
					
				} else {
					rolePermission1 = fetch3(rolePermission);
					if (rolePermission1 == null) {
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						return oldRolePermission;
					}

				}

			} else {
				if (oldRolePermission.getRole().equals(rolePermission.getRole())
						&& oldRolePermission.getModuleName().equals(rolePermission.getModuleName())
						&& oldRolePermission.getSubModuleName().equals(rolePermission.getSubModuleName())) {
					
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

						return oldRolePermission;

					
				} else {

					rolePermission1 = fetch4(rolePermission);
					if (rolePermission1 == null) {
						oldRolePermission.setUpdatedTime(CalendarCalculator.getTimeStamp());
						oldRolePermission.setIsActive("N");
						session.update(oldRolePermission);

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
		query = sessionFactory.getCurrentSession().createQuery("FROM RolePermission WHERE role=:role and moduleName=:moduleName  AND isActive='Y'");
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
		query = sessionFactory.getCurrentSession().createQuery(
				"FROM RolePermission WHERE role=:role and moduleName=:moduleName and subModuleName=:subModuleName  AND isActive=:isActive");
		query.setParameter("subModuleName", rolePermission.getSubModuleName());
		query.setParameter("role", rolePermission.getRole());
		query.setParameter("moduleName", rolePermission.getModuleName());
		query.setParameter("isActive", "Y");
		rolePermissionList = (RolePermission) query.uniqueResult();
		Loggers.loggerEnd();
		return rolePermissionList;
	}

	/*private RolePermission fetch2(RolePermission rolePermission) {
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
*/
	/*public RolePermission fetch(RolePermission rolePermission) {

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

	}*/

	/**
	 * removes the permission entity from the database.
	 * 
	 * @param permission
	 *            instanceOf {@link Permission}
	 * @return Nothing
	 */
	public void deletePermission(RolePermission permission) throws GSmartBaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {


			permission.setExitTime(CalendarCalculator.getTimeStamp());
			permission.setIsActive("D");
			session.update(permission);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")

	@Override
	public Map<String, Object> getPermission(String role) throws GSmartDatabaseException {
		Loggers.loggerStart();

//		List<RolePermission> rolePermissions = new ArrayList<>();
		Map<String, Object> rolePermissions=new HashMap<>();
		

		try {


			Loggers.loggerValue("given role is : ", role);
			query = sessionFactory.getCurrentSession().createQuery(
					"SELECT DISTINCT moduleName from RolePermission where role=:role and isActive=:isActive");
			query.setParameter("role", role);
			query.setParameter("isActive", "Y");
			List<String> modules = query.list();
			Loggers.loggerValue("given modules size is : ", modules.size());
			for (String moduleName : modules) {
				RolePermission permission = new RolePermission();
				/*permission.setModuleName(moduleName);
				Loggers.loggerValue("given moduleName is : ", moduleName);*/
				
				if(moduleName.equalsIgnoreCase("maintenance")){
					permission.setModuleName(moduleName);
					rolePermissions.put(moduleName, permission);
				}else{
					rolePermissions.put(moduleName,getPermission(role,moduleName));
				}
				
			}
			


		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd(rolePermissions);
		return rolePermissions;
	}
	
	private RolePermission getPermission(String role, String module) {
		
		Loggers.loggerStart(role);
		Loggers.loggerStart(module);
		
		RolePermission permissions=null;
		try{
		
		query = sessionFactory.getCurrentSession().createQuery("from RolePermission where role=:role and (moduleName=:moduleName or subModuleName=:moduleName) and isActive=:isActive");
		query.setParameter("role", role);
		query.setParameter("moduleName", module);
		query.setParameter("isActive","Y");
		permissions = (RolePermission) query.uniqueResult();

		Loggers.loggerEnd(permissions);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return permissions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermission> getSubModuleNames(String role) throws GSmartDatabaseException {
		Loggers.loggerStart(role);
		List<RolePermission> rolePermissions = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from RolePermission where role=:role and lower(moduleName)=:moduleName and isActive=:isActive");
			query.setParameter("role", role);
			query.setParameter("isActive", "Y");
			query.setParameter("moduleName", "maintenance");
			rolePermissions = (List<RolePermission>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd(rolePermissions);
		return rolePermissions;
	}

	/* For Editing */
	public RolePermission getRolePermission(String entryTime) {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from RolePermission where isActive='Y' and entryTime=:entryTime ");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Roles> getRoles() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Roles> roles=null;
		try {
			query=sessionFactory.getCurrentSession().createQuery("from Roles");
			roles=query.list();
			
		}catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}

		
		return roles;
	}

}
