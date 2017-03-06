package com.gsmart.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.AttendanceDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Attendance;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Autowired
	AttendanceDao attendancedao;

	@Autowired
	ProfileDao profileDao;
	@Autowired
	SearchService searchService;

	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId)
			throws GSmartServiceException {
		List<Map<String, Object>> attendenanceList = null;
		try {
			attendenanceList = attendancedao.getAttendance(startDate, endDate, smartId);
		} catch (Exception e) {
			e.printStackTrace();

		}
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
		// TODO Auto-generated method stub
		List<Map<String, Object>> responseList = new ArrayList<>();
		for (Hierarchy hierarchy : hidList) {
			Map<String, Object> schoolData = new HashMap<>();
			List<Attendance> attendanceList = attendancedao.getAttendanceByhierarchy(date, hierarchy);
			int totalCount = 0;
			int totalPresent = 0;
			Date dateObj = new Date(date * 1000);
			int year = dateObj.getYear();
			Map<String, Profile> profileMap = new HashMap<>();
			List<Profile> profileList = profileDao.getProfileByHierarchyAndYear(hierarchy, year + "-" + (year + 1));
			for(Profile profile : profileList) {
				profileMap.put(profile.getSmartId(), profile);
			}
			ArrayList<String> childsList = searchService.getAllChildSmartId(smartId, profileMap);
			totalCount = childsList.size();
			for (Attendance attendance : attendanceList) {
				for (String childSmartId : childsList) {
					if (childSmartId.equalsIgnoreCase(attendance.getSmartId())) {
						++totalPresent;
						childsList.remove(childSmartId);
						attendanceList.remove(attendance);
					}
				}
			}
			schoolData.put("totalCount", totalCount);
			schoolData.put("totalPresent", totalPresent);
			schoolData.put("hierarchy", hierarchy);
			responseList.add(schoolData);
		}
		return responseList;
	}

}
