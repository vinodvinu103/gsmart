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
import com.gsmart.model.CompoundFeeMaster;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Token;
import com.gsmart.services.FeeMasterServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The FeeMasterController class implements an application that displays list of
 * feeMaster entities, add new feeMaster entity, edit available feeMaster entity
 * and delete available feeMaster entity. these functionalities are provided in
 * {@link FeeMasterServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Controller
@RequestMapping(Constants.FEE_MASTER)
public class FeeMasterController {
	@Autowired
	private FeeMasterServices feeMasterServices;

	@Autowired
	private GetAuthorization getAuthorization;

	@Autowired
	private HierarchyDao hierarchyDao;

	/**
	 * to view {@link FeeMaster} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of feeMaster entities present in the FeeMaster table
	 * @see List
	 * @throws GSmartBaseException
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchfeemaster(@RequestBody FeeMaster feemaster,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = feemaster.getHierarchy().getHid();
		} else {
			hid = tokenObj.getHierarchy().getHid();
		}
		List<FeeMaster> searchfee = null;
		searchfee = feeMasterServices.searchfeemaster(feemaster, hid);
		Map<String, Object> feelist = new HashMap<>();
		feelist.put("feelist", searchfee);
		return new ResponseEntity<>(feelist, HttpStatus.OK);

	}

	@RequestMapping(value = "/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getFee(@PathVariable("min") int min,
			@PathVariable("hierarchy") Long hierarchy, @PathVariable("max") int max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> feeList = null;

		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> permissions = new HashMap<>();

		Long hid = null;

		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;
		} else {
			hid = tokenObj.getHierarchy().getHid();
		}

		feeList = feeMasterServices.getFeeList(hid, min, max);
		if (feeList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("feeList", feeList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");

		}
		Loggers.loggerEnd();

		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}

	/**
	 * provides the access to persist a new feeMaster entity Sets the
	 * {@code timeStamp} using {@link CalendarCalculator}
	 * 
	 * @param feeMaster
	 *            is instance of {@link FeeMaster}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value = "/hierarchy/{hierarchy}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addFee(@PathVariable("hierarchy") Long hierarchy,
			@RequestBody FeeMaster feeMaster, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(feeMaster);

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		if (tokenObj.getHierarchy() == null) {
			feeMaster.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
		} else {
			feeMaster.setHierarchy(tokenObj.getHierarchy());
		}

		CompoundFeeMaster cb = feeMasterServices.addFee(feeMaster);
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

	/**
	 * provide the access to update feeMaster entity
	 * 
	 * @param feeMaster
	 *            instance of {@link FeeMaster}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editFee(@RequestBody FeeMaster feeMaster,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart(feeMaster);

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		FeeMaster cb = null;
		str.length();
		Map<String, Object> respMap = new HashMap<>();

		if ("edit".equals(task)) {
			cb = feeMasterServices.editFee(feeMaster);
			if (cb != null) {
				respMap.put("status", 200);
				respMap.put("message", "Updated Successfully");
			} else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");

			}
		} else {
			feeMasterServices.deleteFee(feeMaster);
			respMap.put("status", 200);
			respMap.put("message", "Deleted Successfully");
		}

		Loggers.loggerEnd();

		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);

	}

	/**
	 * provide the access to delete feeMaster entity
	 * 
	 * @param timeStamp
	 * @return deletion status (success/error) in JSON format
	 * @see IAMResponse
	 */
	/*
	 * @RequestMapping( method = RequestMethod.DELETE) public
	 * ResponseEntity<IAMResponse> deleteFee(@RequestBody FeeMaster feeMaster)
	 * throws GSmartBaseException { Loggers.loggerStart(); IAMResponse myResponse;
	 * feeMaster.setIsActive("D");
	 * feeMaster.setExittime(CalendarCalculator.getTimeStamp());
	 * feeMasterServices.deleteFee(feeMaster); myResponse = new
	 * IAMResponse("success"); Loggers.loggerEnd(); return new
	 * ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK); }
	 */

	/*
	 * @RequestMapping(value = "/upload/{updSmartId}", method = RequestMethod.POST)
	 * public ResponseEntity<IAMResponse> upload(@PathVariable("updSmartId") String
	 * updSmartId,
	 * 
	 * @RequestParam(value = "file") MultipartFile file) throws GSmartBaseException,
	 * IOException { Loggers.loggerStart();
	 * 
	 * FileUpload fileUpload = new FileUpload();
	 * fileUpload.setFeeDetails("feeDetails");
	 * 
	 * byte[] byteArr = file.getBytes(); fileUpload.setFile(byteArr);
	 * feeMasterServices.fileUpload(fileUpload);
	 * 
	 * IAMResponse myResponse; myResponse = new IAMResponse("success");
	 * Loggers.loggerEnd(); return new ResponseEntity<IAMResponse>(myResponse,
	 * HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
	 * public ResponseEntity<Object > download(@PathVariable("fileName") String
	 * fileName) throws GSmartBaseException {
	 * 
	 * Loggers.loggerStart();
	 * 
	 * 
	 * List<FileUpload> src = feeMasterServices.getFile(fileName);
	 * Loggers.loggerEnd(); Loggers.loggerValue(src.get(0));
	 * 
	 * return new ResponseEntity<Object>(src.get(0), HttpStatus.OK); }
	 */

}