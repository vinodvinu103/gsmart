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
import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Holiday;
import com.gsmart.model.Token;
import com.gsmart.services.HolidayServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The HolidayController class implements an application that displays list of
 * holiday entities, add new holiday entity, edit available holiday entity and
 * delete available holiday entity. these functionalities are provided in
 * {@link HolidayServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Controller
@RequestMapping(Constants.HOLIDAY)
public class HolidayController {

	@Autowired
	private HolidayServices holidayServices;

	@Autowired
	private GetAuthorization getAuthorization;

	@Autowired
	private HierarchyDao hierarchyDao;

	/**
	 * to view {@link Holiday} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of holiday entities present in the Holiday table
	 * @see List
	 * @throws GSmartBaseException
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchholiday(@RequestBody Holiday holiday,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = holiday.getHierarchy().getHid();
		} else {
			hid = tokenObj.getHierarchy().getHid();
		}
		List<Holiday> searchHoliday = null;

		searchHoliday = holidayServices.searchHoliday(holiday, hid);
		Map<String, Object> hl = new HashMap<>();
		hl.put("searchHoliday", searchHoliday);

		return new ResponseEntity<>(hl, HttpStatus.OK);

	}

	@RequestMapping(value = "/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getHoliday(@PathVariable("min") Integer min,
			@PathVariable("hierarchy") Long hierarchy, @PathVariable("max") Integer max,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> holidayList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> permissions = new HashMap<>();
		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;
		} else {
			hid = tokenObj.getHierarchy().getHid();
		}
		holidayList = holidayServices.getHolidayList(hid, min, max);
		if (holidayList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("holidayList", holidayList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");

		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	/**
	 * provides the access to persist a new holiday entity Sets the
	 * {@code timeStamp} using {@link CalendarCalculator}
	 * 
	 * @param holiday
	 *            is instance of {@link Holiday}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value = "/hierarchy/{hierarchy}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addHoliday(@PathVariable("hierarchy") Long hierarchy,
			@RequestBody Holiday holiday, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(holiday);

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");
		if (tokenObj.getHierarchy() == null) {
			holiday.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
		} else {
			holiday.setHierarchy(tokenObj.getHierarchy());
		}

		CompoundHoliday ch = holidayServices.addHoliday(holiday);

		if (ch != null) {
			respMap.put("status", 200);
			respMap.put("message", "Saved Successfully");
		} else {
			respMap.put("status", 400);
			respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

	/**
	 * provide the access to update holiday entity
	 * 
	 * @param holiday
	 *            instance of {@link Holiday}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editHoliday(@RequestBody Holiday holiday,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Holiday ch = null;
		Loggers.loggerStart(holiday);

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if ("edit".equals(task)) {
			ch = holidayServices.editHoliday(holiday);
			if (ch != null) {
				respMap.put("status", 200);
				respMap.put("message", "Upadted Successfully");

			} else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}
		} else {
			holidayServices.deleteHoliday(holiday);
			respMap.put("status", 200);
			respMap.put("message", "Deleted Successfully");
		}

		Loggers.loggerEnd(respMap);
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

	/**
	 * provide the access to delete holiday entity
	 * 
	 * @param holiday
	 *            instance of {@link Holiday}
	 * @return deletion status (success/error) in JSON format
	 * @see IAMResponse
	 */

}
