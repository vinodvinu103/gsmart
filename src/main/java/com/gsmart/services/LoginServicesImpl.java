package com.gsmart.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.LoginDao;
import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class LoginServicesImpl implements LoginServices {

	@Autowired
	LoginDao loginDao;

	@Autowired
	ProfileServices profileServices;

	@Autowired
	RolePermissionServices permissionServices;

	@Autowired
	TokenService tokenService;

	@Override
	public Map<String, Object> authenticate(Login login, String tokenNumber) throws GSmartServiceException {

		Loggers.loggerStart(login);

		Map<String, Object> jsonMap = new HashMap<>();
		
		try {

			if (tokenNumber!=null) {
				Token tokenDetails = getTokenDetails(tokenNumber);
				Loggers.loggerValue("User is already logged in", "");
				List<RolePermission> rolePermissions = permissionServices.getPermission(tokenDetails.getRole());
				Profile profile = profileServices.getProfileDetails(tokenDetails.getSmartId());
				jsonMap.put("permissions", rolePermissions);
				jsonMap.put("profile", profile);
				jsonMap.put("token", tokenDetails.getTokenNumber());
				jsonMap.put("result", 0);
				jsonMap.put("message", "welcome back");
			} else {
				Loggers.loggerValue("Authenticationg the user", "");
				int authentication = loginDao.authenticate(login);

				if (authentication == 0) {
					String smartId = login.getSmartId();
					Profile profile = profileServices.getProfileDetails(smartId);
					String role = profile.getRole();
					List<RolePermission> rolePermissions = permissionServices.getPermission(role);
					String token = issueToken(smartId, role);
					jsonMap.put("permissions", rolePermissions);
					jsonMap.put("profile", profile);
					jsonMap.put("token", token);
					jsonMap.put("result", 0);
					jsonMap.put("message", "welcome");
				} else if (authentication == 1) {
					jsonMap.put("result", 1);
					jsonMap.put("data", null);
					jsonMap.put("message", "User Doesnt Exist");
				} else if (authentication == 2) {
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

	private String issueToken(String smartId, String role) throws GSmartServiceException {

		Token token = null;
		String tokenNumber = null;

		try {
			Random random = new SecureRandom();
			tokenNumber = new BigInteger(100, random).toString();

			token = new Token();
			token.setTokenNumber(tokenNumber);
			token.setSmartId(smartId);
			token.setRole(role);

			tokenService.saveToken(token);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			issueToken(smartId, role);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerValue("Token generated: ", tokenNumber);
		return tokenNumber;
	}

	private Token getTokenDetails(String tokenNumber) throws GSmartServiceException {

		return tokenService.getToken(tokenNumber);
	}

}
