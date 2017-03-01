package com.gsmart.model;

import java.util.List;
import java.util.Map;


public class UserDetails {

	Login login;
	Map<String, Profile> profileMap;
	//Profile profile;
	//Profile profile1;
/*	//Profile teamprofile;
*/	LeaveDetails leaveDetails;
	/*Map<String, List<LeaveType>> leaveTypeMap;*/
	//List<LeaveType> leaveTypes;
/*	//List<LeaveType> availableleave;
*/	Map<String, List<LeaveDetails>> leaveDetailsMapList;
	//List<LeaveDetails> leaveDetailsList;
	List<Holiday> holiday;
	/*Map perfTeamChildInfo;
	List perfTeamList;*/
	Map<Integer, Profile> checkMap;
	/*ApplicationForm applicationForm;*/

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public LeaveDetails getLeaveDetails() {
		return leaveDetails;
	}

	public void setLeaveDetails(LeaveDetails leaveDetails) {
		this.leaveDetails = leaveDetails;
	}

	public List<Holiday> getHoliday() {
		return holiday;
	}

	public void setHoliday(List<Holiday> holiday) {
		this.holiday = holiday;
	}

	/*public Map getPerfTeamChildInfo() {
		return perfTeamChildInfo;
	}

	public void setPerfTeamChildInfo(Map perfTeamChildInfo) {
		this.perfTeamChildInfo = perfTeamChildInfo;
	}

	public List getPerfTeamList() {
		return perfTeamList;
	}

	public void setPerfTeamList(List perfTeamList) {
		this.perfTeamList = perfTeamList;
	}*/

	public Map<Integer, Profile> getCheckMap() {
		return checkMap;
	}

	public void setCheckMap(Map<Integer, Profile> checkMap) {
		this.checkMap = checkMap;
	}

	/*public ApplicationForm getApplicationForm() {
		return applicationForm;
	}

	public void setApplicationForm(ApplicationForm applicationForm) {
		this.applicationForm = applicationForm;
	}*/

	public Profile getProfileMap(String key) {
		return profileMap.get(key);
	}

	public void setProfileMap(String key,Profile value) {
		this.profileMap.put(key, value);
	}

	/*public List<LeaveType> getLeaveTypeMap(String key) {
		return leaveTypeMap.get(key);
	}

	public void setLeaveTypeMap(String key,List<LeaveType> value) {
		this.leaveTypeMap.put(key, value);
	}*/

	public List<LeaveDetails> getLeaveDetailsMap(String key) {
		return leaveDetailsMapList.get(key);
	}

	public void setLeaveDetailsMap(String key, List<LeaveDetails> value) {
		this.leaveDetailsMapList.put(key, value);
	}

}
