package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
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
	SearchService searchService;

	@Autowired
	ReportCardDao reportCardDao;

	@RequestMapping(value="/{academicYear}/{examName}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getList(@RequestHeader HttpHeaders token, HttpSession httpSession,@PathVariable("academicYear") String academicYear,@PathVariable("examName") String examName)
			throws GSmartBaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String, Object> permission = new HashMap<>();
			// String teacherSmartId=smartId.getSmartId();
			Loggers.loggerStart();
				list = reportCardService.search(tokenObj,academicYear,examName);
				permission.put("reportCard", list);
			
		Loggers.loggerEnd();
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
				Token tokenObj=(Token) httpSession.getAttribute("token");
				card.setHierarchy(tokenObj.getHierarchy());
				card.setReportingManagerId(tokenObj.getSmartId());
				card2 = reportCardService.addReportCard(card);
				if (card2 != null)
					iamResponse = new IAMResponse("success");
				else
					iamResponse = new IAMResponse("Oops...! Record Already Exist");
				Loggers.loggerEnd();
			
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

				if (task.equals("edit")) {
					card2 = reportCardService.editReportCard(card);
					if (card2 != null)
						response = new IAMResponse("success");
					else
						response = new IAMResponse("Oops...! Record Already Exist");
				} else if (task.equals("delete")) {
					reportCardService.deleteReportCard(card);
				}
			
		return new ResponseEntity<IAMResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/excelToDB/{smartId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> excelToDB(@PathVariable("smartId") String smartId,
			@RequestBody MultipartFile fileUpload,@RequestHeader HttpHeaders token,
			HttpSession httpSession) {
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, String> jsonMap = new HashMap<>();
			try {
				reportCardService.excelToDB(smartId, fileUpload);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonMap.put("result", "success");

			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		

	}

	@RequestMapping(value = "/getStructure/{smartId}/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getReportCardStructure(@PathVariable("smartId") String smartId,@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart();
		System.out.println("smartid..." + smartId);
		Loggers.loggerValue("token", token);
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerValue("tokenNumber", tokenNumber);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String, Object> resultmap = new HashMap<String, Object>();


		try {
			Profile profile = profileServices.getProfileDetails(smartId);

			Map<String, Profile> profiles = (Map<String, Profile>) searchService.getAllProfiles(academicYear,tokenObj.getHierarchy().getHid());

			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);
			if (childList.size() != 0) {
				System.out.println("list of child***********"+childList.size());
				profile.setChildFlag(true);
				Loggers.loggerStart(profile);
			}
			// befor it is without argument List<ReportCard> allReportCards = reportCardDao.reportCardList();
			List<ReportCard> allReportCards = reportCardDao.reportCardList(tokenObj);
			ArrayList<ReportCard> childReportCards = new ArrayList<>();
			for (int i = 0; i < childList.size(); i++) {

				for (int j = 0; j < allReportCards.size(); j++) {

					if (childList.get(i).getSmartId().equals(allReportCards.get(j).getSmartId())) {

						childReportCards.add(allReportCards.get(j));
						profile.setParentFlag(true);

					}
				}
			}
			Map<String, ArrayList<ReportCard>> customReport = new HashMap<>();
			customReport.put(smartId, childReportCards);
			System.out.println("reportcard-----------"+customReport);
			reportCardService.calculatPercentage(smartId, childReportCards);
			
			//HashMap<String, ArrayList<ReportCard>> childOfChild = new HashMap<String, ArrayList<ReportCard>>();
			Set<String> key = profiles.keySet();
			ArrayList<ReportCard> childOfChildReport = null;
			int flag = 0;

			for (int i = 0; i < childList.size(); i++) {
				childOfChildReport = new ArrayList<ReportCard>();
				for (String j : key) {
					Profile p = (Profile) profiles.get(j);
					if (p != null && p.getReportingManagerId() != null)
						if (p.getReportingManagerId().equals(childList.get(i).getSmartId())) {
							childList.get(i).setChildFlag(true);
							if (p.getSmartId() != childList.get(i).getSmartId()) {
								
								for (int k = 0; k < childReportCards.size(); k++) {
									if (childList.get(i).getSmartId() == childReportCards.get(k).getSmartId()) {
										childReportCards.get(k).setChildReportFlag(1);
										childOfChildReport.add(childReportCards.get(k));
										flag = 1;

										profile.setParentFlag(true);

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

			resultmap.put("selfProfile", profile);
			resultmap.put("childList", childList);
			resultmap.put("childReportCards", customReport);

			Loggers.loggerEnd(resultmap);
			return new ResponseEntity<Map<String, Object>>(resultmap, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, Object> jsonDetails = new HashMap<>();
			jsonDetails.put("result", null);
			return new ResponseEntity<Map<String, Object>>(resultmap, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/academicYear",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getYear(@RequestHeader HttpHeaders token,HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		List<ReportCard> academicYearAndExam = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String, Object> permission = new HashMap<>();
				academicYearAndExam=reportCardDao.acdemicYearAndExamName(tokenObj);
				permission.put("academicYearAndExam", academicYearAndExam);
			
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	}
	
	@RequestMapping(value="/examName/{academicYear}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getExam(@RequestHeader HttpHeaders token,HttpSession httpSession,@PathVariable("academicYear") String academicYear)throws GSmartBaseException{
		Loggers.loggerStart();
		
		List<ReportCard> examName = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String, Object> permission = new HashMap<>();
				examName=reportCardDao.examName(tokenObj,academicYear);
				permission.put("examName", examName);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getReportList(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String, Object> permission = new HashMap<>();
				list = reportCardDao.reportCardList(tokenObj);
				permission.put("reportCard", list);
		Loggers.loggerEnd(list);
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);

	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public ResponseEntity<Map<String, String>> generatePDF(@RequestHeader HttpHeaders token,HttpSession httpSession){
		Loggers.loggerStart();
		
		Loggers.loggerEnd();
		return null;
	}
}
