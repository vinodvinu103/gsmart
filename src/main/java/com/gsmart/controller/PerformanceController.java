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

import com.gsmart.model.CompoundPerformanceAppraisal;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.PerformanceAppraisalService;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.PERFORMANCE)
public class PerformanceController {

	@Autowired
	private PerformanceAppraisalService appraisalservice;
	@Autowired
	private GetAuthorization getauthorization;
	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = "/{year}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAppraisalList(@PathVariable("year") String year,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<PerformanceAppraisal> appraisalList = null;
		RolePermission modulePermission = getauthorization.authorizationForGet(tokenNumber, httpSession);
		Map<String, Object> permissions = new HashMap<>();
		permissions.put("modulePermission", modulePermission);
		Token token1 = tokenService.getToken(tokenNumber);
		String smartId = token1.getSmartId();
		if (modulePermission != null) {

			appraisalList = appraisalservice.getAppraisalList(smartId, year);
			permissions.put("appraisalList", appraisalList);
			Loggers.loggerEnd(appraisalList);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addAppraisal(@RequestBody PerformanceAppraisal appraisal,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(appraisal);
		IAMResponse resp = new IAMResponse();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		if (getauthorization.authorizationForPost(tokenNumber, httpSession)) {
			CompoundPerformanceAppraisal cb = appraisalservice.addAppraisal(appraisal);

			if (cb != null)
				resp.setMessage("success");
			else {
				resp.setMessage("Already exists");
				System.out.println("record exist....");
			}
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editPerformance(@RequestBody PerformanceAppraisal appraisal,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getauthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getauthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))

				appraisalservice.editAppraisal(appraisal);

			else if (task.equals("delete"))

				appraisalservice.deleteAppraisal(appraisal);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}
}
