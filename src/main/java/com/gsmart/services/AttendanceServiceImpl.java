package com.gsmart.services;

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

	@Override
	public List<Map<String, Object>> getAttendance(Long startDate, Long endDate, String smartId) throws GSmartServiceException {
		 List<Map<String, Object>> attendenanceList=null;
		try{
			attendenanceList=attendancedao.getAttendance(startDate, endDate, smartId);
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		return attendenanceList;
	}

	@Override
	public Attendance addAttedance(List<Attendance> attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		Attendance attend=null;
		try{
			attend=attendancedao.addAttendance(attendance);
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return attend;
	}

	@Override
	public void editAttedance(Attendance attendance) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
	         attendancedao.editAttendance(attendance);
		}catch (Exception e) {
          e.printStackTrace();
		}
		Loggers.loggerEnd();
		
	}

	@Override
	public List<Map<String, Object>> getAttendanceByhierarchy(Long date, List<Hierarchy> hidList) {
		// TODO Auto-generated method stub
		List<Attendance> attendanceList = attendancedao.getAttendanceByhierarchy(date);
		List<Map<String, Object>> responseList = new ArrayList<>();
		for(Hierarchy hierarchy : hidList) {
			Map<String, Object> schoolData = new HashMap<>();
			int totalCount = 0;
			int totalPresent = 0;
			Date dateObj = new Date(date * 1000);
			int year = dateObj.getYear();
			List<Profile> profileList = profileDao.getProfileByHierarchyAndYear(hierarchy, year + "-" + (year + 1));
			totalCount = profileList.size();
			for(Attendance attendance : attendanceList) {
				for(Profile profile : profileList) {
					if(profile.getRfId().equalsIgnoreCase(attendance.getRfId())) {
						++totalPresent;
						profileList.remove(profile);
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
