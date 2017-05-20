package com.gsmart.controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import com.gsmart.dao.AttendanceDao;
import com.gsmart.dao.DashboardDao;
import com.gsmart.dao.HolidayDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Holiday;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.services.AttendanceService;
import com.gsmart.services.FeeServices;
import com.gsmart.services.HierarchyServices;
import com.gsmart.services.InventoryAssignmentsServices;
import com.gsmart.services.InventoryServices;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller

@RequestMapping(Constants.DASHBOARD)
public class DashboardController {
	
	@Autowired
	private InventoryServices inventoryServices;
	@Autowired
	private GetAuthorization getAuthorization;
	@Autowired
	private HierarchyServices hierarchyServices;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private SearchService searchService;
	@Autowired
	FeeServices feeServices;
	@Autowired
	ProfileServices profileServices;
	@Autowired
	ReportCardDao reportDao;
	@Autowired
	DashboardDao dashboardDao;
	@Autowired
	HolidayDao holidayDao;
	@Autowired
	private AttendanceDao attendancedao;
	@Autowired
	private ProfileDao profileDao;
	@Autowired
	private InventoryAssignmentsServices inventoryAssignmentServices;

	
	/*@RequestMapping(value = "/inventory/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory1(@PathVariable("academicYear") String academicYear,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<Inventory> inventoryList = new ArrayList<>();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
			
			if(tokenObj.getHierarchy()==null){
				
				List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
				Loggers.loggerStart(hierarchyList);
				for (Hierarchy hierarchy : hierarchyList) {
					Map<String, Object> dataMap = new HashMap<>();
					System.out.println("in side for >>>>>>>>>>>>>>>>>>>   >>>>>>>>>>>>>>>>>. "+hierarchy.getSchool());
					inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), hierarchy);
					dataMap.put("inventoryList", inventoryList);
					dataMap.put("hierarchy", hierarchy);
					inventoryByHierarchy.add(dataMap);
				}
				finalResponse.put("inventoryList", inventoryByHierarchy);
				responseMap.put("data", finalResponse);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
			}else{
				Map<String, Object> dataMap = new HashMap<>();
				System.out.println("in side else condition   <><<><><<><><><><  >>>");
				inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy());
				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", tokenObj.getHierarchy());
				inventoryByHierarchy.add(dataMap);
				finalResponse.put("inventoryList", inventoryByHierarchy);
				responseMap.put("data", finalResponse);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
			}
			Loggers.loggerEnd(inventoryByHierarchy);
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		} */
	@RequestMapping(value = "/inventory/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory(@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> responseMap = new HashMap<>();
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		
		
		List<Inventory> inventoryList = null;
		if (tokenObj.getHierarchy() == null) {
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {
				Map<String, Object> dataMap = new HashMap<>();
				Map<String, Profile> allProfiles = searchService.getAllProfiles(academicYear,
						hierarchy.getHid());
				ArrayList<String> childsList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
				childsList.add(tokenObj.getSmartId());
				
				inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), hierarchy);
				System.out.println("Inventory list::::::::::::::"+inventoryList);
				
				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", hierarchy);
				inventoryByHierarchy.add(dataMap);
			}
			finalResponse.put("inventoryList", inventoryByHierarchy);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {
			Map<String, Object> dataMap = new HashMap<>();
			Map<String, Profile> allProfiles = searchService.getAllProfiles(academicYear, tokenObj.getHierarchy().getHid());
			ArrayList<String> childsList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
			childsList.add(tokenObj.getSmartId());
			inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy());
			dataMap.put("inventoryList", inventoryList);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			inventoryByHierarchy.add(dataMap);
			finalResponse.put("inventoryList", inventoryByHierarchy);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		}
		Loggers.loggerEnd(responseMap);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	

	/*@RequestMapping(value = "/inventory1/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory(@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> responseMap = new HashMap<>();
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		
		List<InventoryAssignments> inventoryAssignmentList = null;
		List<Inventory> inventoryList = null;
		if (tokenObj.getHierarchy() == null) {
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {
				Map<String, Object> dataMap = new HashMap<>();
				Map<String, Profile> allProfiles = searchService.getAllProfiles(academicYear,
						hierarchy.getHid());
				ArrayList<String> childsList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
				childsList.add(tokenObj.getSmartId());
				inventoryAssignmentList = inventoryAssignmentServices.getInventoryDashboardData(childsList, hierarchy);
				inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), hierarchy);
				System.out.println("Inventory list::::::::::::::"+inventoryList);
				inventoryAssignmentList = inventoryAssignmentServices.groupCategoryAndItem(inventoryAssignmentList, inventoryList);
				dataMap.put("inventoryAssignmentList", inventoryAssignmentList);
				dataMap.put("hierarchy", hierarchy);
				inventoryByHierarchy.add(dataMap);
			}
			finalResponse.put("inventoryList", inventoryByHierarchy);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {
			Map<String, Object> dataMap = new HashMap<>();
			Map<String, Profile> allProfiles = searchService.getAllProfiles(academicYear, tokenObj.getHierarchy().getHid());
			ArrayList<String> childsList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
			childsList.add(tokenObj.getSmartId());
			inventoryAssignmentList = inventoryAssignmentServices.getInventoryDashboardData(childsList, tokenObj.getHierarchy());
			inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy());
			inventoryAssignmentList = inventoryAssignmentServices.groupCategoryAndItem(inventoryAssignmentList, inventoryList);
			dataMap.put("inventoryAssignmentList", inventoryAssignmentList);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			inventoryByHierarchy.add(dataMap);
			finalResponse.put("inventoryList", inventoryByHierarchy);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		}
		Loggers.loggerEnd(responseMap);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
*/
	@RequestMapping(value = "/attendance/{date}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAttendance(@PathVariable("date") Long date,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		Loggers.loggerStart("Given date is : " + date);
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Hierarchy> hierarchyList = new ArrayList<>();
		Map<String, Object> responseMap = new HashMap<>();
		List<Map<String,  Object>> attendanceList=null;
		if (tokenObj.getHierarchy() == null) {
			System.out.println("in side if");
			hierarchyList = hierarchyServices.getAllHierarchy();
			System.out.println("hid for admiin <><><>   "+hierarchyList.size());
		} else {
			System.out.println("in side else");
			hierarchyList.add(tokenObj.getHierarchy());
		}
		if(tokenObj.getRole().equalsIgnoreCase("FINANCE")){
			Profile profile=profileDao.getProfileDetails(tokenObj.getSmartId());
			attendanceList=attendanceService.getAttendanceByhierarchy(profile.getReportingManagerId(), date, hierarchyList);
			//attendanceList=attendanceService.getAttendanceByhierarchyForFinance(tokenObj.getSmartId(), date, hierarchyList);
		}
		else if(tokenObj.getRole().equalsIgnoreCase("HR")){
			Profile profile=profileDao.getProfileDetails(tokenObj.getSmartId());
			attendanceList=attendanceService.getAttendanceByhierarchy(profile.getReportingManagerId(), date, hierarchyList);
			//attendanceList=attendanceService.getAttendanceByhierarchyForHr(tokenObj.getSmartId(), date, hierarchyList);
		}
		else{
			attendanceList=attendanceService.getAttendanceByhierarchy(tokenObj.getSmartId(), date, hierarchyList);
		}
		
		responseMap.put("message", "success");
		responseMap.put("status", 200);
		responseMap.put("data", attendanceList);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fee/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> gettotalpaidfee(@PathVariable("academicYear") String academincYear, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("token");
		str.length();

		int totalPaidFees=0;
		int totalFees=0;
		List<Map<String, Object>> responseList = new ArrayList<>();

		Map<String, Object> responseMap = new HashMap<>();
		
		if (tokenObj.getHierarchy() == null) {
			System.out.println("in side if condition for fees");
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {

				Map<String, Object> dataMap = new HashMap<>();

				Map<String, Profile> allProfiles = searchService.getAllProfiles(academincYear,
						hierarchy.getHid());

				List<String> childList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
				childList.add(tokenObj.getSmartId());
				totalPaidFees = feeServices.getTotalFeeDashboard(academincYear, hierarchy.getHid(), childList);
				totalFees = feeServices.getPaidFeeDashboard(academincYear, hierarchy.getHid(), childList);
				dataMap.put("totalPaidFees", totalPaidFees);
				dataMap.put("hierarchy", hierarchy);
				dataMap.put("totalFees", totalFees);
				responseList.add(dataMap);
			}
			responseMap.put("data", responseList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {

			Map<String, Object> dataMap = new HashMap<>();

			Map<String, Profile> allProfiles = searchService.getAllProfiles(academincYear,
					tokenObj.getHierarchy().getHid());
			List<String> childList=null;
			if(tokenObj.getRole().equalsIgnoreCase("FINANCE")){
				Profile profile=profileDao.getProfileDetails(tokenObj.getSmartId());
				childList=searchService.getAllChildSmartId(profile.getReportingManagerId(), allProfiles);
				//childList = searchService.getAllChildSmartIdForFinanceFee(tokenObj.getSmartId(), allProfiles);
			}
			else if(tokenObj.getRole().equalsIgnoreCase("HR")){
				Profile profile=profileDao.getProfileDetails(tokenObj.getSmartId());
				childList=searchService.getAllChildSmartId(profile.getReportingManagerId(), allProfiles);
				//childList = searchService.getAllChildSmartIdForHrFee(tokenObj.getSmartId(), allProfiles);
			}
			else{
				childList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
			}
			childList.add(tokenObj.getSmartId());
			totalPaidFees = feeServices.getTotalFeeDashboard(academincYear, tokenObj.getHierarchy().getHid(), childList);
			totalFees = feeServices.getPaidFeeDashboard(academincYear, tokenObj.getHierarchy().getHid(), childList);
			dataMap.put("totalPaidFees", totalPaidFees);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			dataMap.put("totalFees", totalFees);
			responseList.add(dataMap);
			responseMap.put("data", responseList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else {
			System.out.println("in side else condition for fees");
			responseMap.put("data", null);
			responseMap.put("status", 404);
			responseMap.put("message", "Data not found");
		}
		System.out.println("The paid fees passed here is: "+totalPaidFees);
		System.out.println("The total fees passed here is"+totalFees);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/assign/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventoryAssign(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("token");
		str.length();
		System.out.println("coming");
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		Map<String, Object> inventoryList = null;
		System.out.println("inside the if condition");
		if (tokenObj.getHierarchy() == null) {
			System.out.println("hierarchy is null");
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			System.out.println("going inside for loop");
			for (Hierarchy hierarchy : hierarchyList) {

				inventoryList = dashboardDao.getInventoryAssignList(tokenObj.getRole(), tokenObj.getSmartId(), hierarchy, min, max);
                System.out.println("going to the map");
				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", hierarchy);
				Loggers.loggerEnd("Inventory List:" + inventoryList);

			}
			responseMap.put("data", dataMap);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {

			inventoryList = dashboardDao.getInventoryAssignList(tokenObj.getRole(), tokenObj.getSmartId(), tokenObj.getHierarchy(), min, max);

			dataMap.put("inventoryList", inventoryList);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			responseMap.put("data", dataMap);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else {
			responseMap.put("data", null);
			responseMap.put("status", 404);
			responseMap.put("message", "Data not found");
		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);

	}
	
	@RequestMapping(value="/view/{academicYear}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> search(@RequestBody Profile profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession,@PathVariable("academicYear") String academicYear) throws GSmartBaseException {

		Loggers.loggerStart(academicYear);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		Token tokenObj = (Token) httpSession.getAttribute("token");
		
		List<Profile> profileList = null;
		Loggers.loggerStart(tokenObj);
		
		
		Calendar cal=Calendar.getInstance();
		
		
		Calendar cal2= new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 0);
		Date date = cal2.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1);
		Long startDate = calendar.getTimeInMillis() / 1000;
		Long endDate = date.getTime() / 1000;
		System.out.println(" start date >>>>>>>>>>>>>>>>>>>"+startDate);
		System.out.println(" nd date >>>>>>>>>>>>>>>>>>>"+endDate);
		
		List<Fee> feeList = null;
		List<ReportCard> examName = null;
        
		Map<String, Object> detailMap = new HashMap<>();
		String smartId=profile.getSmartId();
		
		Fee fee=new Fee();
		fee.setAcademicYear(academicYear);
		fee.setSmartId(smartId);
		
		feeList = feeServices.getDashboardFeeList(fee, profile.getHierarchy().getHid());
			profileList = dashboardDao.searchStudentByName(profile, tokenObj.getHierarchy());
			List<Map<String, Object>> attendanceList=attendanceService.getPresentAttendance(startDate, endDate, smartId);
			List<Map<String, Object>> absentList=attendancedao.getAbsentAttendance(startDate, endDate, smartId);
			System.out.println("attendenceList"+attendanceList);
			System.out.println("smartId"+smartId);
			System.out.println("hid of select user"+profile.getHierarchy().getHid());
		
			
			
			examName = dashboardDao.examName(tokenObj, smartId, academicYear);
			
			detailMap.put("profileList", profileList);
			detailMap.put("attendanceList", attendanceList);
			detailMap.put("absentList", absentList);
			detailMap.put("feeList", feeList);
			detailMap.put("examName", examName);
			
			System.out.println("profileList>>>>>>>>>>>>>>>>>>>>>>>>"+profileList);
			System.out.println("attendanceList>>>>>>>>>>>>>>>>>>>>>>>>"+attendanceList);
			System.out.println("feeList>>>>>>>>>>>>>>>>>>>>>>>>"+feeList);
			System.out.println("report examName>>>>>>>>>>>>>>>>>>>>>>>>"+examName);
			
			Loggers.loggerEnd(detailMap);
		 return new ResponseEntity<Map<String, Object>>(detailMap, HttpStatus.OK);
	}
	
	@RequestMapping(value="academicYear/{smartId}",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> academicYear(@PathVariable("smartId") String smartId,@RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		Token tokenObj=(Token) httpSession.getAttribute("token");
		List<Fee> academicYear=null;
		Map<String, Object> permission = new HashMap<>();
				academicYear=dashboardDao.academicYear(tokenObj,smartId);
				permission.put("academicYear", academicYear);
			
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	}
	
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
		List<Map<String, Object>> absentList = null;
		List<Holiday> holidayList = null;
		Calendar cal = new GregorianCalendar(year, month, 0);
		Date date = cal.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		Long startDate = calendar.getTimeInMillis() / 1000;
		Long endDate = date.getTime() / 1000;
		attendanceList = attendanceService.getPresentAttendance(startDate, endDate, smartId);
		absentList=attendancedao.getAbsentAttendance(startDate, endDate, smartId);
		holidayList = holidayDao.holidayList(tokenObj.getHierarchy().getHid());


		permissions.put("attendanceList", attendanceList);
		permissions.put("absentList", absentList);
		System.out.println("attendanceList:" + attendanceList);
		permissions.put("holidayList", holidayList);

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}
	
	@RequestMapping(value="/studentprofile", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> studentProfile(@RequestHeader HttpHeaders token, HttpSession httpSession){
		
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		Token tokenObj=(Token) httpSession.getAttribute("token");
		
		Map<String,Object> profileMap=new HashMap<>();
		List<Profile> profileList=null;
		profileList=dashboardDao.studentProfile(tokenObj,tokenObj.getHierarchy());
		System.out.println("STUDENT>>>>>>>>"+profileList);
		profileMap.put("profileList", profileList);
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String,Object>>(profileMap, HttpStatus.OK);
	}
	
	@RequestMapping(value="/searchbyname", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchByName(@RequestBody Profile profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Profile> studentList = null;
        
		
		Map<String, Object> privilege = new HashMap<>();
			studentList = dashboardDao.searchStudentByName(profile, tokenObj.getHierarchy());
			
			System.out.println("studentList>>>>>>>>>>>>>>>>>>>>>>>>>>>>::"+studentList);
			
			privilege.put("studentList", studentList);
			Loggers.loggerEnd(studentList);
		 return new ResponseEntity<Map<String, Object>>(privilege, HttpStatus.OK);
	}
	@RequestMapping(value="/searchbyId", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchById(@RequestBody Profile profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Profile> studentList = null;
        
		
		Map<String, Object> privilege = new HashMap<>();
			studentList = dashboardDao.searchStudentById(profile, tokenObj.getHierarchy());
			
			System.out.println("Student by ID list (controller): "+studentList);
			privilege.put("studentList", studentList);
			Loggers.loggerEnd(studentList);
		 return new ResponseEntity<Map<String, Object>>(privilege, HttpStatus.OK);
	}
}
