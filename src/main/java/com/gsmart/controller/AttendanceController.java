package com.gsmart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsmart.dao.HolidayDao;
import com.gsmart.model.Attendance;
import com.gsmart.model.Holiday;
import com.gsmart.model.Profile;
import com.gsmart.model.SyncRequestObject;
import com.gsmart.model.Token;
import com.gsmart.services.AttendanceService;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.ATTENDANCE)
public class AttendanceController {

	@Autowired
	private GetAuthorization getAuthorization;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private SearchService searchService;

	@Autowired
	private ProfileServices profileServices;

	@Autowired
	private HolidayDao holidayDao;

	@RequestMapping(value = "/calendar/{month}/{year}/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAttendance(@RequestHeader HttpHeaders token, HttpSession httpSession,
			@PathVariable("month") Integer month, @PathVariable("year") Integer year, 
			@PathVariable("smartId") String smartId, Holiday holiday) throws GSmartBaseException {

		Loggers.loggerStart();

		Map<String, Object> permissions = new HashMap<>();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		List<Map<String, Object>> attendanceList = null;
		List<Holiday> holidayList = null;
		Calendar cal = new GregorianCalendar(year, month, 0);
		Date date = cal.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		Long startDate = calendar.getTimeInMillis() / 1000;
		Long endDate = date.getTime() / 1000;
		attendanceList = attendanceService.getAttendance(startDate, endDate, smartId);

		holidayList = holidayDao.holidayList(tokenObj.getHierarchy().getHid());


		permissions.put("attendanceList", attendanceList);
		System.out.println("attendanceList:" + attendanceList);
		permissions.put("holidayList", holidayList);

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<Map<String, Object>> addAttendance(@RequestBody String stringObj) throws GSmartBaseException {

		Loggers.loggerStart(stringObj);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			SyncRequestObject syncRequestObject = objectMapper.readValue(stringObj, SyncRequestObject.class);
			List<String> rfidList = attendanceService.addAttedance(syncRequestObject.getRequestMap().get("attendanceList"));
			if (rfidList == null) {
				responseMap.put("status", 500);
			} else {
				responseMap.put("data", rfidList);
				responseMap.put("status", 200);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editAttendance(@RequestBody Attendance attendance,
	@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException {

		Loggers.loggerStart();
		IAMResponse myResponse = null;

		String tokenNumber = token.get("Authorization").get(0);
		System.out.println("token Number" + tokenNumber);
		System.out.println("task is " + task);
		System.out.println("httpSession" + httpSession);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
			if (task.equals("edit")) {
				attendanceService.editAttedance(attendance);

			}
			myResponse = new IAMResponse("success");
			Loggers.loggerEnd(attendance);
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> orgStructureController(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		int year = Calendar.getInstance().get(Calendar.YEAR);  // Gets the current date and time
		String academicYear=year+"-"+(year+1);


		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> resultmap = new HashMap<String, Object>();

			Profile profile = profileServices.getProfileDetails(smartId);
			Map<String, Profile> profiles = searchService.getAllProfiles(academicYear,tokenObj.getHierarchy().getHid());
			Loggers.loggerValue("profile is ", profile);
			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);
			Loggers.loggerValue("child is", childList);
			
			List<String> childListForAttendance = searchService.getAllChildSmartId(tokenObj.getSmartId(), profiles);

			resultmap.put("attendanceCount", attendanceService.getAttendanceCount(childListForAttendance));
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
	
	
}
