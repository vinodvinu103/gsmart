package com.gsmart.util;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.TokenService;

@Component
public class GetAuthorization {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	TokenService tokenService;

	Session session;
	Transaction tx;
	Query query;

	public RolePermission authorizationForGet(String tokenNumber, HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart(tokenNumber);
		RolePermission permissions = null;

		try {
			Token token = tokenService.getToken(tokenNumber);
			String module = getModuleName();
			Loggers.loggerValue("Token: ", token);
			Loggers.loggerValue("Module: ", module);
			permissions = getPermission(token, module);
			httpSession.setAttribute("permissions", permissions);

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerEnd(permissions);
		return permissions;
	}

	public boolean authorizationForPost(String tokenNumber, HttpSession httpSession) {
		
		Loggers.loggerStart(tokenNumber);
		RolePermission rolePermission = null;
		
		try{
			rolePermission = (RolePermission) httpSession.getAttribute("permissions");
			Loggers.loggerEnd(rolePermission.getAdd());
			return rolePermission.getAdd();
		} catch(Exception e){
			e.printStackTrace();
			Loggers.loggerEnd(false);
			return false;
		}
	
	}

	public boolean authorizationForPut(String tokenNumber, String task, HttpSession httpSession) {
		
		Loggers.loggerStart(tokenNumber);
		Loggers.loggerStart(task);
		RolePermission rolePermission = null;
		
		try{
			rolePermission = (RolePermission) httpSession.getAttribute("permissions");
			
			if(task.equalsIgnoreCase("edit"))
			{
				Loggers.loggerEnd(rolePermission.getEdit());
				return rolePermission.getEdit();
			}
			else
			{
				Loggers.loggerEnd(rolePermission.getDel());
				return rolePermission.getDel();
			}
		} catch(Exception e){
			e.printStackTrace();
			Loggers.loggerEnd(false);
			return false;
		}
	}

	private String getModuleName() {

		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stacktrace[3];
		String[] str = element.getClassName().split("\\.");
		int length = str.length;
		String moduleName = str[length-1].replace("Controller", "");
		System.out.println(moduleName);
		return moduleName;
	}
	
	private RolePermission getPermission(Token token, String module) {
		
		Loggers.loggerStart(token);
		Loggers.loggerStart(module);
		
		RolePermission permissions;
		session = sessionFactory.openSession();
		query = session.createQuery("from RolePermission where role=:role and (moduleName=:moduleName or subModuleName=:moduleName)");
		query.setParameter("role", token.getRole());
		query.setParameter("moduleName", module);
		permissions = (RolePermission) query.uniqueResult();
		
		Loggers.loggerEnd(permissions);
		return permissions;
	}
	
	public String getAuthentication(String tokenNumber, HttpSession httpSession){
		
		Loggers.loggerStart(tokenNumber);
		if(tokenNumber.equals(httpSession.getAttribute("tokenNumber")))
			return "Success";
		else
			return null;
	}
}
