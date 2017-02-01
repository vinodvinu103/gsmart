package com.gsmart.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.LoginDao;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;


@Service
public class BeanFactory {
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private RolePermissionServices permissionServices;
	
	
	private Map<String, Profile> beanFactory;
	private Map<String, Boolean> childList;
	private Map<String, Map<String, RolePermission>> customizePermissions;
	

	Logger logger = Logger.getLogger(BeanFactory.class);

	public void getAllBeanFactory() {
		/*try {
			boolean flag = true;
			beanFactory = new HashMap<String, Profile>();
			//get all active records in profile table
			List<Profile> profiles = loginDao.getAllRecord();

			childList = new HashMap<String, Boolean>();
			
			 puts profile data corresponding to its smartId and sets the flag
			for (Profile profile : profiles) {
				beanFactory.put(profile.getSmartId(), profile);
				
				
				if (profile.getReportingManagerId() != null && flag) {
					if (profile.getSmartId() == profile.getReportingManagerId()) {
						childList.put(profile.getSmartId(), true);
						flag = false;
					}
				}
				childList.put(profile.getSmartId(), false);
			}
			 gets the active leave details 
			
			
			customizePermissions = new HashMap<String, Map<String, RolePermission>>();

			Map<String, RolePermission> moduleMap = new HashMap<String, RolePermission>();
			//get active role permissions
			List<RolePermission> rolePermissions = permissionServices.getPermissionList();
			for (RolePermission rolePermission : rolePermissions) {
				if (customizePermissions.containsKey(rolePermission.getRole())) {
					moduleMap = customizePermissions.get(rolePermission.getRole());
					if (!moduleMap.containsKey(rolePermission.getModuleName())) {
						moduleMap.put(rolePermission.getModuleName(), rolePermission);
						customizePermissions.put(rolePermission.getRole(), moduleMap);
					}
				} else {
					moduleMap = new HashMap<String, RolePermission>();
					moduleMap.put(rolePermission.getModuleName(), rolePermission);
					customizePermissions.put(rolePermission.getRole(), moduleMap);
				}
				moduleMap = new HashMap<String, RolePermission>();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public Map<String, Profile> getBeanFactory() {
		getAllBeanFactory();
		return beanFactory;
	}



	

	

	

}
