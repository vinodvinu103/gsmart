package com.gsmart.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.AttendanceDao;
import com.gsmart.dao.HolidayDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Attendance;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

	

	@Autowired
	private AttendanceDao attendancedao;
	
	
	@Autowired
	private ProfileDao profileDao;
	@Autowired
	private SearchService searchService;

	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartServiceException {
		List<Map<String, Object>> attendenanceList = null;
		try {
			attendenanceList = attendancedao.getAttendance(startDate, endDate, smartId);
		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("attendance service"+attendenanceList);
		return attendenanceList;
	}
	@Override
	public List<Map<String, Object>> getPresentAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartServiceException {
		List<Map<String, Object>> attendenanceList = null;
		try {
			attendenanceList = attendancedao.getPresentAttendance(startDate, endDate, smartId);
		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("attendance service"+attendenanceList);
		return attendenanceList;
	}

	@Override
	public List<String> addAttedance(List<Attendance> attendanceList) throws GSmartServiceException {
		Loggers.loggerStart();
		List<String> rfidList = null;
		try {
			rfidList = attendancedao.addAttendance(attendanceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return rfidList;
	}

	@Override
	public void editAttedance(Attendance attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			attendancedao.editAttendance(attendance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();

	}

	@Override
	public List<Map<String, Object>> getAttendanceByhierarchy(String smartId, Long date, List<Hierarchy> hidList) {
		List<Map<String, Object>> responseList = new ArrayList<>();
		try {
			
			for (Hierarchy hierarchy : hidList) {
				Map<String, Object> schoolData = new HashMap<>();
				List<Attendance> attendanceList = attendancedao.getAttendanceByhierarchy(date, hierarchy);
				Loggers.loggerStart(attendanceList);
				int totalCount = 0;
				int totalPresent = 0;
				// Date dateObj = new Date(date * 1000);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Map<String, Profile> profileMap = new HashMap<>();
				List<Profile> profileList = profileDao.getProfileByHierarchyAndYear(hierarchy, year + "-" + (year + 1));
				Loggers.loggerStart(profileList);
				for(Profile profile : profileList) {
					profileMap.put(profile.getSmartId(), profile);
				}
				ArrayList<String> childsList = searchService.getAllChildSmartId(smartId, profileMap);
				Loggers.loggerStart(childsList);
				totalCount = childsList.size();
				System.out.println("total child count"+totalCount);
				for (Attendance attendance : attendanceList) {
					System.out.println("in side 1st for loop attendance>>>>>>>>>>>>>>>>>>>>>>>>>>");
					for (String childSmartId : childsList) {
						System.out.println("in side 2nd  for loop child list >>  >  >>>>   >>>>>>");
						if (childSmartId.equalsIgnoreCase(attendance.getSmartId())) {
							System.out.println("if condition to clculate");
							++totalPresent;
							System.out.println("total child present ><><><><  "+totalPresent);
							// childsList.remove(childSmartId);
						// 	attendanceList.remove(attendance);
						}
					}
				}
				schoolData.put("totalCount", totalCount);
				schoolData.put("totalPresent", totalPresent);
				schoolData.put("hierarchy", hierarchy);
				responseList.add(schoolData);
			}
			Loggers.loggerEnd(responseList);
			return responseList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseList;
	}
	
	@Override
	public List<Map<String, Object>> getAttendanceByhierarchyForFinance(String smartId, Long date, List<Hierarchy> hidList) {
		List<Map<String, Object>> responseList = new ArrayList<>();
		try {
			
			for (Hierarchy hierarchy : hidList) {
				Map<String, Object> schoolData = new HashMap<>();
				List<Attendance> attendanceList = attendancedao.getAttendanceByhierarchy(date, hierarchy);
				Loggers.loggerStart(attendanceList);
				int totalCount = 0;
				int totalPresent = 0;
				// Date dateObj = new Date(date * 1000);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Map<String, Profile> profileMap = new HashMap<>();
				List<Profile> profileList = profileDao.getProfileByHierarchyAndYear(hierarchy, year + "-" + (year + 1));
				Loggers.loggerStart(profileList);
				for(Profile profile : profileList) {
					profileMap.put(profile.getSmartId(), profile);
				}
				ArrayList<String> childsList = searchService.getAllChildSmartIdForFinance(smartId, profileMap);
				Loggers.loggerStart(childsList);
				totalCount = childsList.size();
				System.out.println("total child count"+totalCount);
				for (Attendance attendance : attendanceList) {
					System.out.println("in side 1st for loop attendance>>>>>>>>>>>>>>>>>>>>>>>>>>");
					for (String childSmartId : childsList) {
						System.out.println("in side 2nd  for loop child list >>  >  >>>>   >>>>>>");
						if (childSmartId.equalsIgnoreCase(attendance.getSmartId())) {
							System.out.println("if condition to clculate");
							++totalPresent;
							System.out.println("total child present ><><><><  "+totalPresent);
							// childsList.remove(childSmartId);
						// 	attendanceList.remove(attendance);
						}
					}
				}
				schoolData.put("totalCount", totalCount);
				schoolData.put("totalPresent", totalPresent);
				schoolData.put("hierarchy", hierarchy);
				responseList.add(schoolData);
			}
			Loggers.loggerEnd(responseList);
			return responseList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseList;
	}
	
	@Override
	public List<Map<String, Object>> getAttendanceByhierarchyForHr(String smartId, Long date, List<Hierarchy> hidList) {
		List<Map<String, Object>> responseList = new ArrayList<>();
		try {
			
			for (Hierarchy hierarchy : hidList) {
				Map<String, Object> schoolData = new HashMap<>();
				List<Attendance> attendanceList = attendancedao.getAttendanceByhierarchy(date, hierarchy);
				Loggers.loggerStart(attendanceList);
				int totalCount = 0;
				int totalPresent = 0;
				// Date dateObj = new Date(date * 1000);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Map<String, Profile> profileMap = new HashMap<>();
				List<Profile> profileList = profileDao.getProfileByHierarchyAndYear(hierarchy, year + "-" + (year + 1));
				Loggers.loggerStart(profileList);
				for(Profile profile : profileList) {
					profileMap.put(profile.getSmartId(), profile);
				}
				ArrayList<String> childsList = searchService.getAllChildSmartIdForHr(smartId, profileMap);
				Loggers.loggerStart(childsList);
				totalCount = childsList.size();
				System.out.println("total child count"+totalCount);
				for (Attendance attendance : attendanceList) {
					System.out.println("in side 1st for loop attendance>>>>>>>>>>>>>>>>>>>>>>>>>>");
					for (String childSmartId : childsList) {
						System.out.println("in side 2nd  for loop child list >>  >  >>>>   >>>>>>");
						if (childSmartId.equalsIgnoreCase(attendance.getSmartId())) {
							System.out.println("if condition to clculate");
							++totalPresent;
							System.out.println("total child present ><><><><  "+totalPresent);
							// childsList.remove(childSmartId);
						// 	attendanceList.remove(attendance);
						}
					}
				}
				schoolData.put("totalCount", totalCount);
				schoolData.put("totalPresent", totalPresent);
				schoolData.put("hierarchy", hierarchy);
				responseList.add(schoolData);
			}
			Loggers.loggerEnd(responseList);
			return responseList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseList;
	}

	@Override
	public Map<String, Object> getAttendanceCount(List<String> childList,Date date) {
		try {
			Loggers.loggerStart();
			
			return attendancedao.getAttendanceCount(childList,date);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public void addClassAttendance(List<Attendance> attendance) throws GSmartDatabaseException {
		Loggers.loggerStart();
		attendancedao.addClassAttendance(attendance);
		
	}

}
