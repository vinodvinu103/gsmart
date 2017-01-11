package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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

import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.BeanFactory;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.ReportCardService;
import com.gsmart.services.SearchService;
import com.gsmart.services.TokenService;
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

	@Autowired
	TokenService tokenService;

	@Autowired
	ProfileServices profileServices;

	@Autowired
	BeanFactory beanFactory;

	@Autowired
	SearchService searchService;

	@Autowired
	ReportCardDao reportCardDao;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getList(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> permission = new HashMap<>();
		permission.put("modulePremission", modulePermission);
		try {
			Token tokenDetail = tokenService.getToken(tokenNumber);
			// String teacherSmartId=smartId.getSmartId();
			Loggers.loggerStart();
			if (modulePermission.getView()) {
				list = reportCardService.search(tokenDetail);
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

	
	@RequestMapping(value = "/getStructure/{smartId}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> getReportCardStructure(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart();
		Loggers.loggerValue("token", token);
		Map<String, Object> permissions = new HashMap<String, Object>();
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerValue("tokenNumber", tokenNumber);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermissions = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		permissions.put("modulePermissions", modulePermissions);
		
		/*Map<String, Object> jsonMap = new HashMap<String, Object>();*/
		try {
			Profile profile = profileServices.getProfileDetails(smartId);

			Map<String, Profile> beanFactory1 = beanFactory.getBeanFactory();
			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, beanFactory1);
			if (childList.size() != 0) {
				profile.setChildFlag(true);
			}
			System.out.println("child size=="+childList.size());
			List<ReportCard> allReportCards = reportCardDao.reportCardList();
			System.out.println(allReportCards);
			ArrayList<ReportCard> childReportCards = new ArrayList<>();
			for (int i = 0; i < childList.size(); i++) {
				System.out.println("in side first for loop=="+childList.size());
				System.out.println("allReportCards size=="+allReportCards.size());
				for (int j = 0; j < allReportCards.size(); j++) {
					System.out.println("in side 2nd for loop");
					System.out.println( childList.get(i).getSmartId()+"   "+allReportCards.get(j).getSmartId());
					/*if (childList.get(i).getSmartId() == allReportCards.get(j).getSmartId()) {
						System.out.println("in side if conditon");
						childReportCards.add(allReportCards.get(j));
					}*/
				
					if (childList.get(i).getSmartId().equals( allReportCards.get(j).getSmartId())) {
						System.out.println("in side if conditon");
						childReportCards.add(allReportCards.get(j));
					}
				}
			}
			Map<String, ArrayList<ReportCard>> customReport = new HashMap<>();
			customReport.put(smartId, childReportCards);
			
			Loggers.loggerStart(">>><<<<<"+childReportCards);

			HashMap<String, ArrayList<ReportCard>> childOfChild = new HashMap<String, ArrayList<ReportCard>>();
			Set<String> key = beanFactory1.keySet();
			ArrayList<ReportCard> childOfChildReport = null;
			int flag = 0;

			for (int i = 0; i < childList.size(); i++) {
				childOfChildReport = new ArrayList<ReportCard>();

				for (String j : key) {
					Profile p = (Profile) beanFactory1.get(j);
					if (p != null && p.getReportingManagerId() != null)
						if (p.getReportingManagerId() == childList.get(i).getSmartId()) {
							if (p.getSmartId() != childList.get(i).getSmartId()) {
								for (int k = 0; k < childReportCards.size(); k++) {
									if (childList.get(i).getSmartId() == childReportCards.get(k).getSmartId()) {
										childReportCards.get(k).setChildReportFlag(1);
										childOfChildReport.add(childReportCards.get(k));
										flag = 1;
									}
								}
							}
						}
				}
				flag = 0;
			}

			Map<String, Object> parentInfo = searchService.getParentInfo(smartId);
			if (parentInfo.get("parentProfile") != null && parentInfo.get("reportingProfiles") != null) {
				profile.setParentFlag(true);
			}

			Map<String, Object> resultmap = new HashMap<String, Object>();
			resultmap.put("selfProfile", profile);
			resultmap.put("childList", childList);
			resultmap.put("childReportCards", customReport);
			permissions.put("result", resultmap);
			Loggers.loggerEnd(permissions);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, Object> jsonDetails = new HashMap<>();
			jsonDetails.put("result", null);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}
}
