package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.hibernate.jpa.internal.schemagen.GenerationSourceFromMetadata;
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

import com.gsmart.model.Attendance;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
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
	GetAuthorization getAuthorization;

	@Autowired
	AttendanceService attendanceService;

	@Autowired
	SearchService searchService;

	@Autowired
	ProfileServices profileServices;

	@RequestMapping(/*value = "/calender/{startdate}/{enddate}",*/ method = RequestMethod.GET)
	public ResponseEntity<List<Attendance>> getAttendance(@RequestHeader HttpHeaders token/*, HttpSession httpSession,
			@PathVariable long startdate, @PathVariable long enddate*/) throws GSmartBaseException {
		Loggers.loggerStart();

		/*Map<String, Object> permissions = new HashMap<>();*/

		List<Attendance> attendanceList = null;

		/*String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		permissions.put("modulePermission", modulePermission);

		if (modulePermission != null)*/ {
			/*if (startdate == 0 & enddate == 0)*/
				attendanceList = attendanceService.getAttendance();
			/*else
				attendanceList = attendanceService.sortAttendance(startdate, enddate);
			permissions.put("atteendanceList", attendanceList);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);*/
		}
		Loggers.loggerEnd(attendanceList);
		return new ResponseEntity<List<Attendance>>(attendanceList, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAssigningReportee(@RequestBody Attendance attendance,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(attendance);

		IAMResponse rsp = null;
		Map<String, Object> response = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {

			Attendance attend = attendanceService.addAttedance(attendance);
			if (attend != null) {
				rsp = new IAMResponse("success");
				response.put("message", rsp);
			} else {
				rsp = new IAMResponse("Data Already exists");
				response.put("message", rsp);

			}
		} else {
			rsp = new IAMResponse("Permission Denied");
			response.put("message", rsp);

		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteBand(@RequestBody Attendance attendance,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit")) {
				attendanceService.editAttedance(attendance);

			} else if (task.equals("delete")) {
				attendanceService.deleteAttendance(attendance);
			}
			myResponse = new IAMResponse("success");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}
	

	@RequestMapping(value = "/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> orgStructureController(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermisson = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> resultmap = new HashMap<String, Object>();

		resultmap.put("modulePermisson", modulePermisson);
		if (modulePermisson != null) {
			Profile profile = profileServices.getProfileDetails(smartId);
			Map<String, Profile> profiles = searchService.getAllProfiles();
            Loggers.loggerValue("profile is ",profile);
			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);
			Loggers.loggerValue("child is",childList);

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
		} else {
			return new ResponseEntity<Map<String, Object>>(resultmap, HttpStatus.OK);
		}

	}
}
