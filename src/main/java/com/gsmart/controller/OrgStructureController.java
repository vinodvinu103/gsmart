package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Search;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;

@Controller
@RequestMapping(value = "/org")
public class OrgStructureController {

	@Autowired
	SearchService searchService;

	@Autowired
	ProfileServices profileServices;
	
	@Autowired
	GetAuthorization getAuthorization;

	//private static final Logger logger = Logger.getLogger(OrgStructureController.class);

	@RequestMapping(value = "/{smartId}")
	public ResponseEntity<Map<String, Object>> orgStructureController(@PathVariable("smartId") String smartId ,@RequestHeader HttpHeaders token,HttpSession httpSession)
			throws GSmartBaseException {

		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		RolePermission modulePermisson=getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
		Map<String, Object> resultmap = new HashMap<String, Object>();
		
		resultmap.put("modulePermisson", modulePermisson);
		if(modulePermisson!=null)
		{
		Profile profile = profileServices.getProfileDetails(smartId);
		Map<String, Profile> profiles = searchService.getAllProfiles();

		ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);

		if (childList.size() != 0) {
			profile.setChildFlag(true);
		}

		Set<String> key = profiles.keySet();
		for (int i = 0; i < childList.size(); i++) {

			for (String j : key) {

				Profile p = (Profile) profiles.get(j);
				if (p.getReportingManagerId().equals(childList.get(i).getSmartId())) {

					if (!(p.getSmartId().equals(childList.get(i).getSmartId()))) {
						childList.get(i).setChildFlag(true);
					}
				}
			}
		}
		
		
		resultmap.put("selfProfile", profile);
		resultmap.put("childList", childList);
		return new ResponseEntity<Map<String, Object>>(resultmap, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Map<String, Object>>(resultmap, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/searchname", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ArrayList<Profile>>> Search(@RequestBody Search search,@RequestHeader HttpHeaders token,HttpSession httpSession) {
		{

			String tokenNumber=token.get("Authorization").get(0);
			String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
			str.length();
			
			getAuthorization.authorizationForPost(tokenNumber, httpSession);
			Map<String, ArrayList<Profile>> jsonMap = new HashMap<String, ArrayList<Profile>>();
			try {
				if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){
				Map<String, Profile> map = searchService.getAllProfiles();
				ArrayList<Profile> profiless = searchService.getEmployeeInfo(search.getName(), map);
				jsonMap.put("result", profiless);
				return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
				}
			} catch (Exception e) {
				jsonMap.put("result", null);
				return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
			}

		}
	}
/*
	@RequestMapping(value = "/searchRep", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<Profile>>> searchRep(@RequestBody Search search) {
		{
			Map<String, ArrayList<Profile>> jsonMap = new HashMap<String, ArrayList<Profile>>();
			try {

				Map<String, Profile> map = searchService.searchRep(search);
				logger.info(map.size());
				ArrayList<Profile> profiless = searchService.getEmployeeInfo(search.getName(), map);
				jsonMap.put("result", profiless);
				return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				jsonMap.put("result", null);
				return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
			}

		}

	}*/

	@RequestMapping(value = "/search/{smartId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<Profile>>> searchRep(@RequestBody Search search,
			@PathVariable("smartId") String smartId) throws GSmartBaseException {

		Map<String, ArrayList<Profile>> jsonMap = new HashMap<String, ArrayList<Profile>>();

		ArrayList<Profile> temp = new ArrayList<Profile>();

		Map<String, Profile> profiles = searchService.getAllProfiles();

		ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);

		ArrayList<Profile> temp1 = childList;

		ArrayList<Profile> temp2 = new ArrayList<>();

		while (!(temp1.isEmpty())) {

			for (int i = 0; i < temp1.size(); i++) {

				smartId = temp1.get(i).getSmartId();
				temp = searchService.searchEmployeeInfo(smartId, profiles);
				temp2.clear();
				temp2.addAll(temp);
				childList.addAll(temp);
			}
			temp1 = temp2;

		}
		ArrayList<Profile> list = new ArrayList<Profile>();

		for (Profile p : childList) {
			if ((p.getSmartId().trim().toLowerCase()).startsWith(search.getName().toLowerCase())) {
				list.add(p);
			}
			else if ((p.getFirstName().trim().toLowerCase()).startsWith(search.getName().toLowerCase())) {
				list.add(p);
			}
			if ((p.getRole().trim().toLowerCase()).startsWith(search.getName().toLowerCase())) {
				list.add(p);
			}
		}
		
		
		Set<String> key = profiles.keySet();
		for (int i = 0; i < list.size(); i++) {

			for (String j : key) {

				Profile p = (Profile) profiles.get(j);
				if (p.getReportingManagerId().equals(list.get(i).getSmartId())) {

					if (!(p.getSmartId().equals(list.get(i).getSmartId()))) {
						list.get(i).setChildFlag(true);
					}
				}
			}
		}
				
		
		jsonMap.put("childList", list);

		return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
	}
}
