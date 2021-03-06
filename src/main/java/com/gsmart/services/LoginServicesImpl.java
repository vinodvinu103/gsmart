package com.gsmart.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.HierarchyDao;
import com.gsmart.dao.LoginDao;
import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class LoginServicesImpl implements LoginServices{

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private ProfileServices profileServices;

	@Autowired
	private RolePermissionServices permissionServices;

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private HierarchyDao hierarchyDao;

	@Override
	public Map<String, Object> authenticate(Login login, String tokenNumber) throws GSmartServiceException {

		Loggers.loggerStart(login);

		Map<String, Object> jsonMap = new HashMap<>();
		
		try {

			if (tokenNumber!=null) {
				Token tokenDetails = getTokenDetails(tokenNumber);
				System.out.println("role is"+tokenDetails.getRole());
				Loggers.loggerValue("User is already logged in", "");
				
				Map<String, Object> rolePermissions = permissionServices.getPermission(tokenDetails.getRole());
				Profile profile = profileServices.getProfileDetails(tokenDetails.getSmartId());
				
				if(tokenDetails.getHierarchy()!=null){
					jsonMap.put("hierarchy", hierarchyDao.getHierarchyByHid(tokenDetails.getHierarchy().getHid()));
				}else{
					jsonMap.put("hierarchy", null);
				}
				jsonMap.put("permissions", rolePermissions);
				jsonMap.put("profile", profile);
				jsonMap.put("token", tokenDetails.getTokenNumber());
				jsonMap.put("tokenObj", tokenDetails);
				jsonMap.put("result", 0);
				jsonMap.put("message", "welcome back");
			} else {
				Loggers.loggerValue("Authenticationg the user", "");
				Map<String, Object> authentication = loginDao.authenticate(login);

				if (authentication != null && (int)authentication.get("status") == 0) {
					String smartId = login.getSmartId();
					Login loginObj = ((Login)authentication.get("login"));
					Profile profile = profileServices.getProfileDetails(smartId);
					String role = profile.getRole();
					String reportingId = profile.getReportingManagerId();
	
					Map<String, Object> rolePermissions = permissionServices.getPermission(role);
					Token token = issueToken(smartId, role, loginObj,reportingId);
					
					if(loginObj.getHierarchy()!=null){
						jsonMap.put("hierarchy", hierarchyDao.getHierarchyByHid(loginObj.getHierarchy().getHid()));
					}else{
						jsonMap.put("hierarchy", null);
					}
					jsonMap.put("permissions", rolePermissions);
					jsonMap.put("profile", profile);
					jsonMap.put("token", token.getTokenNumber());
					jsonMap.put("tokenObj", token);
					jsonMap.put("result", 0);
					jsonMap.put("message", "welcome");
				} else if (authentication != null && (int)authentication.get("status") == 1) {
					jsonMap.put("result", 1);
					jsonMap.put("data", null);
					jsonMap.put("message", "User Doesnt Exist");
				} else if (authentication != null && (int)authentication.get("status") == 2) {
					jsonMap.put("result", 2);
					jsonMap.put("data", null);
					jsonMap.put("message", "account is locked, contact admin");
				} else {
					jsonMap.put("result", 3);
					jsonMap.put("data", null);
					jsonMap.put("message", "Invalid Password");
				}
			}
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerEnd();
		return jsonMap;
	}

	private Token issueToken(String smartId, String role, Login loginObj,String reportingId) throws GSmartServiceException {

		Token token = null;
		String tokenNumber = null;

		try {
			Random random = new SecureRandom();
			tokenNumber = new BigInteger(100, random).toString();

			token = new Token();
			token.setTokenNumber(tokenNumber);
			token.setSmartId(smartId);
			token.setRole(role);
			token.setReportingManagerId(reportingId);
			token.setHierarchy(loginObj.getHierarchy());
			tokenService.saveToken(token, loginObj);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
//			issueToken(smartId, role, loginObj,reportingId);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerValue("Token generated: ", token);
		return token;
	}

	private Token getTokenDetails(String tokenNumber) throws GSmartServiceException {

		return tokenService.getToken(tokenNumber);
	}

}
