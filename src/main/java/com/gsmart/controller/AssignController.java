package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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

import com.gsmart.dao.HierarchyDao;
import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Token;
import com.gsmart.services.AssignService;
import com.gsmart.services.ProfileServices;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.ASSIGN)
public class AssignController {

	@Autowired
	private GetAuthorization getAuthorization;

	@Autowired
	private AssignService assignService;

	@Autowired
	private HierarchyDao hierarchyDao;

	@Autowired
	private ProfileServices profileServices;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchassign(@RequestBody Assign assign,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = assign.getHierarchy().getHid();
		} else {
			hid = tokenObj.getHierarchy().getHid();
		}
		List<Assign> searchAssign = null;
		searchAssign = assignService.searchassign(assign, hid);
		Map<String, Object> assignlist = new HashMap<>();
		assignlist.put("searchassign", searchAssign);
		return new ResponseEntity<>(assignlist, HttpStatus.OK);

	}

	@RequestMapping(value = "/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAssigningReportee(@PathVariable("min") Integer min,
			@PathVariable("hierarchy") Long hierarchy, @PathVariable("max") Integer max,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> permissions = new HashMap<>();

		Map<String, Object> assignList = null;

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		Long hid = null;

		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;

		} else {
			hid = tokenObj.getHierarchy().getHid();
		}

		assignList = assignService.getAssignReportee(hid, min, max);
		if (assignList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("assignList", assignList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");

		}
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	@RequestMapping(value = "/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAssigningList(@PathVariable("hierarchy") Long hierarchy,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);

		Map<String, Object> permissions = new HashMap<>();

		List<Assign> assignList = null;

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		Long hid = null;

		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;

		} else {
			hid = tokenObj.getHierarchy().getHid();
		}
		assignList = assignService.getAssignList(hid);
		if (assignList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("assignList", assignList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");

		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	@RequestMapping(value = "/hierarchy/{hierarchy}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAssigningReportee(@RequestBody Assign assign,
			@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");
		if (tokenObj.getHierarchy() == null) {
			assign.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
		} else {
			assign.setHierarchy(tokenObj.getHierarchy());

		}

		CompoundAssign cb = assignService.addAssigningReportee(assign);
		if (cb != null) {
			respMap.put("status", 200);
			respMap.put("message", "Saved Successfully");
		}

		else {
			respMap.put("status", 400);
			respMap.put("message", "Data Already Exist, Please try with SomeOther Data");

		}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editDeleteBand(@RequestBody Assign assign,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		Assign ch = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> respMap = new HashMap<>();

		if ("edit".equals(task)) {
			ch = assignService.editAssigningReportee(assign);
			if (ch != null) {
				respMap.put("status", 200);
				respMap.put("message", "Upadted Successfully");

			} else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}
		} else {
			assignService.deleteAssigningReportee(assign);
			respMap.put("status", 200);
			respMap.put("message", "Deleted Successfully");
		}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/staff", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getStaffs(@RequestBody Hierarchy hierarchy,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> response = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		response.put("staffList", profileServices.getProfileByHierarchy(hierarchy));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/searchstandardfee/{hierarchy}/{standard}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> searchStandardFee(@PathVariable("standard") String standard,
			@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(standard);

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonResult = new HashMap<>();
		boolean result = assignService.searchStandardFeeService(standard, hierarchy);
		if (result) {
			jsonResult.put("status", 500);
			jsonResult.put("message", "Please.. fill FeeMaster before registering the student ");
		} else {
			jsonResult.put("status", 200);
			jsonResult.put("message", "success");
		}
		return new ResponseEntity<Map<String, Object>>(jsonResult, HttpStatus.OK);

	}

}
