package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
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
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.ReportCard;
import com.gsmart.model.RolePermission;
import com.gsmart.services.ReportCardService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.REPORTCARD)
public class ReportCardController {

	@Autowired
	ReportCardService reportCardService;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	LoginController loginController;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getList(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Map<String, Object> permission = null;
		permission.put("modulePremission", modulePermission);
		try {
			if (modulePermission != null) {
				list = reportCardService.reportCardList();
				permission.put("reportCard", list);
				return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new GSmartBaseException(e.getMessage());
		}
		Loggers.loggerEnd(list);
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addReportCard(@RequestBody ReportCard card, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(card);
		IAMResponse iamResponse = null;
		CompoundReportCard card2 = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		try {
			if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
				card2 = reportCardService.addReportCard(card);
				if (card2 != null)
					iamResponse = new IAMResponse("success");
				else
					iamResponse = new IAMResponse("Oops...! Record Already Exist");
				Loggers.loggerEnd();
			}
		} catch (Exception e) {
			throw new GSmartBaseException(e.getMessage());
		}
		return new ResponseEntity<IAMResponse>(iamResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteReportCard(@RequestBody ReportCard card,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(card);
		IAMResponse response = null;
		ReportCard card2 = null;

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		try {
			if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
				if (task.equals("edit")) {
					card2 = reportCardService.editReportCard(card);
					if (card2 != null)
						response = new IAMResponse("success");
					else
						response = new IAMResponse("Oops...! Record Already Exist");
				} else if (task.equals("delete")) {
					reportCardService.deleteReportCard(card);
				}
			}
		} catch (ConstraintViolationException e) {
			throw new GSmartBaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartBaseException(e.getMessage());
		}
		return new ResponseEntity<IAMResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/excelToDB/{smartId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> excelToDB(@PathVariable("smartId") String smartId,
			@RequestBody MultipartFile fileUpload) {
		Map<String, String> jsonMap = new HashMap<>();
		try {
			reportCardService.excelToDB(smartId, fileUpload);
			jsonMap.put("result", "success");
			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("result", "error");
			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<List<ReportCard>> search(@RequestBody ReportCard card) throws GSmartBaseException {
		String smartId = card.getSmartId();
		/*
		 * String subject = card.getSubject(); int standard =
		 * card.getStandard();
		 */
		System.out.println();
		List<ReportCard> card2 = null;
		try {
			card2 = reportCardService.search(smartId);
			// card2 = reportCardService.search(subject, standard);

		} catch (GSmartServiceException e) {
			e.printStackTrace();
		}
		System.out.println(card2);
		return new ResponseEntity<List<ReportCard>>(card2, HttpStatus.OK);

	}

	@RequestMapping(value = "/subAndStand", method = RequestMethod.POST)
	public ResponseEntity<List<ReportCard>> searchBasedONSubjectAndStandard(@RequestBody ReportCard card)
			throws GSmartBaseException {
		Loggers.loggerStart(card);
		String subject = card.getSubject();
		int standard = card.getStandard();
		System.out.println();
		List<ReportCard> card2 = null;
		try {

			card2 = reportCardService.search(subject, standard);

		} catch (GSmartServiceException e) {
			e.printStackTrace();
		}
		System.out.println(card2);
		return new ResponseEntity<List<ReportCard>>(card2, HttpStatus.OK);
	}

	@RequestMapping(value = "/StandardAndSection", method = RequestMethod.POST)
	public ResponseEntity<List<ReportCard>> searchBasedOnStandard(@RequestBody ReportCard card)
			throws GSmartBaseException {
		Loggers.loggerStart(card);
		System.out.println();
		List<ReportCard> card2 = null;
		try {
			card2 = reportCardService.searchBasedOnStandard(card);
		} catch (GSmartServiceException e) {
			e.printStackTrace();
		}
		System.out.println(card2);
		return new ResponseEntity<List<ReportCard>>(card2, HttpStatus.OK);
	}
}
