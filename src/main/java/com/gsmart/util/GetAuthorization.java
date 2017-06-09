package com.gsmart.util;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;

@Component
public class GetAuthorization {

	@Autowired
	private SessionFactory sessionFactory;


	private Session session;
	private Query query;
	private RolePermission permissions = null;
	private Token tokenObj=null;

	public RolePermission authorizationForGet(String tokenNumber, HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart(tokenNumber);
		

		try {
			Token token=(Token) httpSession.getAttribute("token");
			System.out.println("token"+token);
			String module = getModuleName();
			Loggers.loggerValue("Token: ", token);
			Loggers.loggerValue("Module: ", module);
			permissions = getPermission(token, module);
			httpSession.setAttribute("permissions", permissions);
			httpSession.setAttribute("hierarchy", token);
			System.out.println("hierarchy in token"+httpSession.getAttribute("hierarchy"));
			System.out.println("permission"+permissions);
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
			tokenObj=(Token) httpSession.getAttribute("hierarchy");
			if(rolePermission==null || tokenObj==null)
			{
				Token token=(Token) httpSession.getAttribute("token");
				String module = getModuleName();
				Loggers.loggerValue("Token: ", token);
				Loggers.loggerValue("Module: ", module);
				rolePermission = getPermission(token, module);
				httpSession.setAttribute("rolePermissions", rolePermission);
				httpSession.setAttribute("hierarchy", token);
			Loggers.loggerEnd(rolePermission.getAdd());
			return rolePermission.getAdd();
			}
		} catch(Exception e){
			e.printStackTrace();
			Loggers.loggerEnd(false);
			return false;
		}
		
		Loggers.loggerEnd();
		return rolePermission.getAdd();
	
	}

	public boolean authorizationForPut(String tokenNumber, String task, HttpSession httpSession) {
		
		RolePermission rolePermission = null;
		
		try{
			rolePermission = (RolePermission) httpSession.getAttribute("permissions");
			tokenObj= (Token) httpSession.getAttribute("hierarchy");
			
			if(rolePermission==null || tokenObj==null)
			{
				Token token=(Token) httpSession.getAttribute("token");
				String module = getModuleName();
				Loggers.loggerValue("Token: ", token);
				Loggers.loggerValue("Module: ", module);
				rolePermission = getPermission(token, module);
				httpSession.setAttribute("permissions", rolePermission);
				httpSession.setAttribute("hierarchy", token);
			}

			
			if(task.equalsIgnoreCase("edit"))
			{
				return rolePermission.getEdit();
			}
			else
			{
				return rolePermission.getDel();
			}
		} catch(Exception e){
			e.printStackTrace();
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
		
		RolePermission permissions=null;
		try{
		session = sessionFactory.openSession();
		session.beginTransaction();
		query = session.createQuery("from RolePermission where role=:role and (moduleName=:moduleName or subModuleName=:moduleName) and isActive=:isActive");
		query.setParameter("role", token.getRole());
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
	
	public String getAuthentication(String tokenNumber, HttpSession httpSession){
		
		Loggers.loggerStart(tokenNumber);
		System.out.println("session object"+httpSession.getAttribute("tokenNumber"));
		if(tokenNumber.equals(httpSession.getAttribute("tokenNumber"))){
			Loggers.loggerStart();
			return "Success";
		}
		else
			return null;
	}
}
