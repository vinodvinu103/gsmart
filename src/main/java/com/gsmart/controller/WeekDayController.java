package com.gsmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.WeekDays;
import com.gsmart.services.WeekDaysService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.WEEKDAYS)
public class WeekDayController {

	
	@Autowired
	WeekDaysService weekDaysService;

	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<WeekDays>> getWeekDaysList() throws GSmartBaseException {
		Loggers.loggerStart();
		List<WeekDays> list = null;
		try {
			list = weekDaysService.getWeekDaysList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// add
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IAMResponse> addWeekDays(@RequestBody WeekDays weekDays) throws GSmartBaseException {

		Loggers.loggerStart(weekDays);
		
		IAMResponse rsp = null;
		weekDaysService.addWeekDaysList(weekDays);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}

	// delete

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<IAMResponse> deleteWeekDays(@RequestBody WeekDays weekDays) throws GSmartBaseException {

		Loggers.loggerStart(weekDays);
		IAMResponse rsp = null;
		weekDaysService.deleteWeekdaysList(weekDays);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}

	// update
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> updateWeekDays(@RequestBody WeekDays weekDays) throws GSmartBaseException {

		Loggers.loggerStart(weekDays);
		IAMResponse rsp = null;
		weekDaysService.editWeekdaysList(weekDays);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}

}