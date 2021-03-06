package com.gsmart.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsmart.dao.NoticeDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.services.NoticeService;
import com.gsmart.services.SearchService;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private SearchService searchService;

	@Autowired
	private ProfileDao profileDao;

	@Autowired
	private GetAuthorization getAuthorization;

	@Autowired
	private NoticeDao noticeDao;


	@RequestMapping(value = "/viewNotice/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewNotice(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart("view notice api started in notice controller for id :" + smartId);
		Calendar now = Calendar.getInstance(); // Gets the current date and time
		int year = now.get(Calendar.YEAR); // The current year
		int year1 = year;
		int year2 = ++year;
		String academicYear = year1 + "-" + year2;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		Map<String, Object> responseMap = new HashMap<>();

		List<Notice> list = new ArrayList<Notice>();
		Map<String, Profile> allprofiles = new HashMap<>();

		try {

			if (tokenObj.getRole().equalsIgnoreCase("director") || tokenObj.getRole().equalsIgnoreCase("admin")) {
				list = noticeService.viewAdminNoticeService(smartId);
			} else {
				List<Profile> profiles = profileDao.getAllProfiles(academicYear);
				for (Profile profile : profiles) {

					allprofiles.put(profile.getSmartId(), profile);
				}

				ArrayList<String> parentSmartIdList = searchService.searchParentInfo(smartId, allprofiles);

				parentSmartIdList.remove(smartId);
				list = noticeService.viewNotice(parentSmartIdList, tokenObj.getHierarchy().getHid());
			}

			for (Notice notice : list) {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
				Date d = f.parse(notice.getEntryTime());
				notice.setEntryTime(String.valueOf(d.getTime()));

			}
			responseMap.put("data", list);
			responseMap.put("status", 200);
			responseMap.put("message", "sucess");

			Loggers.loggerEnd("view notice api ended in notice controller for id :" + smartId
					+ " with noticeList size of " + list.size());
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/viewMyNotice/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewMyNotice(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart("viewMyNotice api started in notice controller for id :" + smartId);

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		Map<String, Object> responseMap = new HashMap<>();

		List<Notice> list = new ArrayList<Notice>();

		try {
			list = noticeService.viewMyNotice(smartId, tokenObj.getHierarchy().getHid());
			responseMap.put("data", list);
			responseMap.put("status", 200);
			responseMap.put("message", "sucess");
			Loggers.loggerEnd("viewMyNotice api ended in notice controller for id :" + smartId+" with mynoticeList size of "+list.size());
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
	 * true
	 * 
	 * }
	 */
	@RequestMapping(value = "/generic/{type}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewGenericNotice(@PathVariable("type") String type) {
		Loggers.loggerStart("viewGenericNotice api started in notice controller  of type " + type);

		List<Notice> list = new ArrayList<Notice>();
		Map<String, Object> responeMap = new HashMap<>();

		try {

			list = noticeService.viewGenericNotice(type);
			for (Notice notice : list) {

				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
				Date d = f.parse(notice.getEntryTime());
				notice.setEntryTime(String.valueOf(d.getTime()));
			}
			responeMap.put("data", list);
			responeMap.put("status", 200);
			responeMap.put("message", "success");
			Loggers.loggerEnd("viewGenericNotice api ended in notice controller  of type " + type +" with list size of "+list.size());
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
			Loggers.loggerStart("addNotice api started in notice controller  of type " + notice.getType());
			String tokenNumber = token.get("Authorization").get(0);
			String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
			str.length();
			Map<String, Object> jsonMap = new HashMap<>();
			try {
				Token tokenObj = (Token) httpSession.getAttribute("token");

				// Token token1 = tokenService.getToken(tokenNumber);
				// String smartId = token1.getSmartId();
				if (notice.getHierarchy() == null) {
					notice.setHierarchy(tokenObj.getHierarchy());
				}

				noticeService.addNotice(notice, tokenObj);
				jsonMap.put("status", 200);
				jsonMap.put("result", "success");
				Loggers.loggerEnd("addNotice api ended in notice controller  of type " );
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

			Token tokenObj = (Token) httpSession.getAttribute("token");
			Loggers.loggerStart(notice);
			if (tokenObj.getHierarchy() == null) {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
				Date d = new Date(Long.parseLong(notice.getEntryTime()));

				notice.setEntryTime(f.format(d));
			}

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
		Token tokenObj = (Token) httpSession.getAttribute("token");
		if (tokenObj.getHierarchy() == null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
			Date d = new Date(Long.parseLong(notice.getEntryTime()));

			notice.setEntryTime(f.format(d));
		}

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

	@RequestMapping(value = "/adminNotice/{hid}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewNoticeForAdmin(@PathVariable("hid") Long hid,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		// Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> responseMap = new HashMap<>();
		List<Notice> list = new ArrayList<Notice>();

		try {
			list = noticeDao.viewNoticeForAdmin(hid);
			responseMap.put("data", list);
			responseMap.put("status", 200);
			responseMap.put("message", "sucess");

			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
