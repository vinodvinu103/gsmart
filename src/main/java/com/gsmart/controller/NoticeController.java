package com.gsmart.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsmart.dao.NoticeDao;
import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.NoticeService;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.services.TokenService;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	NoticeService noticeService;

	@Autowired
	TokenService tokenService;

	@Autowired
	SearchService searchService;

	@Autowired
	ProfileServices profileServices;

	@Autowired
	GetAuthorization getAuthorization;

	final Logger logger = Logger.getLogger(NoticeDao.class);

	@RequestMapping(value = "/viewNotice/{smartId}/{year}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewNotice(@PathVariable("smartId") String smartId,
			@PathVariable("year") String year, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartServiceException {

		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();


		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");

		Map<String, Object> responseMap = new HashMap<>();

		List<Notice> list = new ArrayList<Notice>();

		try {
			Map<String, Profile> allprofiles = searchService.getAllProfiles(year,
					tokenObj.getHierarchy().getHid());
			ArrayList<String> parentSmartIdList = searchService.searchParentInfo(smartId, allprofiles);


			parentSmartIdList.remove(smartId);
			list = noticeService.viewNotice(parentSmartIdList);
			System.out.printf("smart id list :", list);
			responseMap.put("data", list);
			responseMap.put("status", 200);
			responseMap.put("message", "sucess");
			responseMap.put("modulePermission", modulePermission);

			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/viewMyNotice/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewMyNotice(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> responseMap = new HashMap<>();

		List<Notice> list = new ArrayList<Notice>();

		try {
			list = noticeService.viewMyNotice(smartId);
			responseMap.put("data", list);
			responseMap.put("status", 200);
			responseMap.put("message", "sucess");
			responseMap.put("modulePermission", modulePermission);
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * @RequestMapping(value = "/{smartId}") public ResponseEntity<Map<String,
	 * Object>> orgStructureController(@PathVariable("smartId") String smartId
	 * ,@RequestHeader HttpHeaders token,HttpSession httpSession) throws
	 * GSmartBaseException {
	 * 
	 * String tokenNumber=token.get("Authorization").get(0); String
	 * str=getAuthorization.getAuthentication(tokenNumber, httpSession);
	 * str.length();
	 * 
	 * RolePermission
	 * modulePermisson=getAuthorization.authorizationForGet(tokenNumber,
	 * httpSession);
	 * 
	 * Map<String, Object> resultmap = new HashMap<String, Object>();
	 * 
	 * resultmap.put("modulePermisson", modulePermisson);
	 * if(modulePermisson!=null) { Profile profile =
	 * profileServices.getProfileDetails(smartId); Map<String, Profile> profiles
	 * = searchService.getAllProfiles();
	 * 
	 * ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId,
	 * profiles);
	 * 
	 * if (childList.size() != 0) { profile.setChildFlag(true); }
	 * 
	 * Set<String> key = profiles.keySet(); for (int i = 0; i <
	 * childList.size(); i++) {
	 * 
	 * for (String j : key) {
	 * 
	 * Profile p = (Profile) profiles.get(j); if
	 * (p.getReportingManagerId().equals(childList.get(i).getSmartId())) {
	 * 
	 * if (!(p.getSmartId().equals(childList.get(i).getSmartId()))) {
	 * childList.get(i).setChildFlag(true); } } } }
	 * 
	 * 
	 * resultmap.put("selfProfile", profile); resultmap.put("childList",
	 * childList); return new ResponseEntity<Map<String, Object>>(resultmap,
	 * HttpStatus.OK); } else { return new ResponseEntity<Map<String,
	 * Object>>(resultmap, HttpStatus.OK); }
	 * 
	 * }
	 */
	/*
	 * @RequestMapping(value = "/childNotice/{smartId}",method =
	 * RequestMethod.GET) public ResponseEntity<Map<String,List<Notice>>>
	 * childNotice(@PathVariable("smartId") String smartId, @RequestHeader
	 * HttpHeaders token,HttpSession httpSession){ Map<String,List<Notice>>
	 * responseMap = new HashMap<>(); List<Notice> list=new ArrayList<Notice>();
	 * Loggers.loggerStart(); Loggers.loggerStart(smartId); try {
	 * 
	 * list=noticeService.childNotice(smartId); responseMap.put("childNotice",
	 * list); return new ResponseEntity<Map<String,List<Notice>>>(responseMap,
	 * HttpStatus.OK); } catch (Exception e){
	 * 
	 * e.printStackTrace(); return null;
	 * 
	 * // TODO: handle exception }
	 * 
	 * 
	 * }
	 */
	@RequestMapping(value = "/generic/{type}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewGenericNotice(@PathVariable("type") String type) {
		Loggers.loggerStart();

		List<Notice> list = new ArrayList<Notice>();
		Map<String, Object> responeMap = new HashMap<>();

		try {
			System.out.println("role coming from frontend" + type);


			list = noticeService.viewGenericNotice(type);
			for (Notice notice : list) {

				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
				Date d = f.parse(notice.getEntryTime());
				notice.setEntryTime(String.valueOf(d.getTime()));
				Loggers.loggerStart("notice.getEntryTime : " + notice.getEntryTime());
			}
			responeMap.put("data", list);
			responeMap.put("status", 200);
			responeMap.put("message", "success");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(responeMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/addNotice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> addNotice(@RequestBody Notice notice, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartServiceException {
		{
			Loggers.loggerStart();
			String tokenNumber = token.get("Authorization").get(0);
			String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
			str.length();
			Map<String, Object> jsonMap = new HashMap<>();
			try {

				Token token1 = tokenService.getToken(tokenNumber);
				// String smartId = token1.getSmartId();

				noticeService.addNotice(notice, token1);
				jsonMap.put("status", 200);
				jsonMap.put("result", "success");

				return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				jsonMap.put("result", "error");
				jsonMap.put("status", 400);
				return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
			}

		}
	}

	@RequestMapping(value = "/editNotice", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, Object>> editNotice(@RequestBody Notice notice,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		{

			Loggers.loggerStart();

			String tokenNumber = token.get("Authorization").get(0);
			String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
			str.length();
			Map<String, Object> jsonMap = new HashMap<>();

			try {
				noticeService.editNotice(notice);
				jsonMap.put("status", 200);
				jsonMap.put("result", "success");
				Loggers.loggerEnd();
				return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
			}

			catch (Exception e) {
				jsonMap.put("result", "error");
				jsonMap.put("status", 400);

				return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
			}

		}
	}

	@RequestMapping(value = "/deleteNotice", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, Object>> deleteNotice(@RequestBody Notice notice,
			@RequestHeader HttpHeaders Token, HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart();

		String tokenNumber = Token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> jsonMap = new HashMap<>();
		try {
			noticeService.deleteNotice(notice);
			jsonMap.put("status", 200);
			jsonMap.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}

		catch (Exception e) {
			jsonMap.put("result", "error");
			jsonMap.put("status", 400);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}

	}

}
