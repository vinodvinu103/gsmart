package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
//import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class SearchServiceImp implements SearchService {

	@Autowired
	ProfileDao profiledao;

	@Autowired
	FeeServices feeServices;

	@Autowired 
	FeeMasterServices feeMasterServices;

	
	private Map<String, Profile> allProfiles;

	@Override
	public Map<String, Profile> getAllProfiles() {
		Loggers.loggerStart();
		allProfiles = new HashMap<String, Profile>();
		List<Profile> profiles = profiledao.getAllRecord();
		Loggers.loggerValue("returnd to getall Profiles in serviceImpl ", "");
		for (Profile profile : profiles) {
			Loggers.loggerValue("smartIds :", profile.getSmartId());
			allProfiles.put(profile.getSmartId(), profile);
		}
		Loggers.loggerEnd("for each loop is executed");
		return allProfiles;
	}

	@Override
	public ArrayList<Profile> getEmployeeInfo(String emp, Map<String, Profile> map) {
		Loggers.loggerStart();
		ArrayList<Profile> list = new ArrayList<Profile>();
		try {
			Set<String> key = map.keySet();
			for (String i : key) {

				Profile p = (Profile) map.get(i);

				if ((p.getSmartId().trim().toLowerCase()).startsWith(emp.toLowerCase())) {
					list.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}

	@Override
	public ArrayList<Profile> searchEmployeeInfo(String smartId, Map<String, Profile> map) {
		Loggers.loggerStart("searchEmployeeInfo ");
		/* Loggers.loggerValue("profiles in map", map); */
		Set<String> key = map.keySet();
		ArrayList<Profile> childList = new ArrayList<Profile>();
		/* Loggers.loggerValue("key", key); */

		for (String temp : key) {
			Profile p = map.get(temp);
			if (p.getReportingManagerId().equals(smartId)) {
				if (!(p.getSmartId().equals(smartId))) {
					childList.add(p);
				}
			}
		}
		Loggers.loggerEnd("searchEmployeeInfo ended");
		return childList;
	}

	public Map<String, Profile> searchRep(Search search) {

		Loggers.loggerStart();
		allProfiles = new HashMap<String, Profile>();

		List<Profile> profiles = profiledao.getsearchRep(search);

		Loggers.loggerValue("", profiles);
		for (Profile profile : profiles) {
			Loggers.loggerValue("entered int for each loop in searchResp", "");
			allProfiles.put(profile.getSmartId(), profile);
		}
		Loggers.loggerEnd();
		return allProfiles;
	}

	@Override
	public ArrayList<String> searchParentInfo(String smartId, Map<String, Profile> map) {

		ArrayList<String> parentList = new ArrayList<String>();

		parentList.add(smartId);

		Profile p;
		boolean temp;
		do {
			Loggers.loggerValue("entered int do while in searchParentInfo", "");
			p = map.get(smartId);
			if (!(p.getReportingManagerId().equals(smartId))) {
				parentList.add(p.getReportingManagerId());
				smartId = p.getReportingManagerId();
				temp = true;
				Loggers.loggerValue("entered into if in do while in searchParentInfo", "");
			} else {
				temp = false;
			}
		} while (temp);
		Loggers.loggerValue("ended do while in searchParentInfo", "");
		return parentList;
	}

	ArrayList<Profile> allChilds = new ArrayList<Profile>();
	ArrayList<Profile> childOfChildList = new ArrayList<Profile>();
	Profile employeeProfile = null;
	ArrayList<Profile> reportingSumUpfees = new ArrayList<Profile>();
	ArrayList<Profile> fees = new ArrayList<Profile>();
	ArrayList<Profile> childOfChild = new ArrayList<Profile>();

	@Override
	public ArrayList<Profile> sumUpFee(ArrayList<Profile> childList, Map<String, Profile> profiles)
			throws GSmartServiceException {
		Loggers.loggerStart();

		if (!childList.isEmpty()) {

			childOfChildList = gotoloop(childList, profiles);

		} else {
			return childList;
		}

		Loggers.loggerEnd(reportingSumUpfees);

		return reportingSumUpfees;
	}

	public ArrayList<Profile> addFees(ArrayList<Profile> profiles) {
		Loggers.loggerStart();
		for (Profile profile : profiles) {
			Map<String, Profile> profileMap = new HashMap<String, Profile>();
			employeeProfile = profiledao.getProfileDetails(profile.getReportingManagerId());
			System.out.println("his profile smartId" + profile.getSmartId());
			System.out.println("reportee id" + employeeProfile.getSmartId());
			profileMap.put(employeeProfile.getSmartId(), employeeProfile);
			System.out.println("profile map" + profileMap);
			reportingSumUpfees = totalfees(profileMap, profiles);
		}
		allChilds.addAll(reportingSumUpfees);

		Loggers.loggerEnd(reportingSumUpfees);

		return reportingSumUpfees;

	}

	public ArrayList<Profile> checkUser(String smartId, ArrayList<Profile> profiles) {
		Loggers.loggerStart();

		if (profiles.get(0).getSmartId().equals(smartId)) {
			System.out.println("smartId from front end and backend eqals");
			return profiles;

		} else {
			reportingSumUpfees = addFees(profiles);
			checkUser(smartId, reportingSumUpfees);
		}

		return reportingSumUpfees;
	}

	public ArrayList<Profile> allchilds() {
		return allChilds;

	}

	@Override
	public ArrayList<Profile> studentFees(ArrayList<Profile> childList) throws GSmartServiceException {

		Loggers.loggerStart(childList);
		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Fee> feeList = feeServices.getFeeLists("2016-2017");

		ArrayList<FeeMaster> fee = (ArrayList<FeeMaster>) feeMasterServices.getFeeList();

		Map<String, Fee> feeMap = new HashMap<String, Fee>();

		Map<String, Integer> feeMasterMap = new HashMap<String, Integer>();

		System.out.println("feelist size is" + feeList.size());

		for (int i = 0; i < feeList.size(); i++) {

			Loggers.loggerValue("entered into 1st For loop ", "");
			feeMap.put(feeList.get(i).getSmartId(), feeList.get(i));
			Loggers.loggerValue("smartid", feeList.get(i).getSmartId());
			Loggers.loggerValue("feelist for above smart id", feeList.get(i));
			Loggers.loggerValue("fee map details", feeMap);
		}

		for (int i = 0; i < fee.size(); i++) {
			Loggers.loggerValue("entered into 2nd For loop ", "");

			feeMasterMap.put(fee.get(i).getStandard(), fee.get(i).getTotalFee());

			Loggers.loggerValue("total standard for ", fee.get(i).getStandard());
			Loggers.loggerValue("total fee for ", fee.get(i).getTotalFee());

		}
		System.out.println("before gng to set fees");

		for (Profile profile : childList) {

			Loggers.loggerValue("entered into for each loop ", "");
			System.out.println(profile.getSmartId());

			Loggers.loggerValue("getting smartId", feeMap.get(profile.getSmartId()));
			if (feeMap.get(profile.getSmartId()) != null) {

				Loggers.loggerValue("entered into if loop inside for each loop ", "");
				profile.setPaidAmount(feeMap.get(profile.getSmartId()).getPaidFee());
				System.out.println("paid fee ");
				profile.setBalanceAmount(feeMap.get(profile.getSmartId()).getBalanceFee());
				System.out.println("Balance fee ");
				profile.setTotalAmount(feeMasterMap.get(profile.getStandard()));
				System.out.println("add profile");

				fees.add(profile);
				Loggers.loggerValue("exiting from if loop inside for each loop ", "");
			} else {
				Loggers.loggerValue("entered into else  ", "");
				profile.setTotalAmount(feeMasterMap.get(profile.getStandard()));

				fees.add(profile);
				Loggers.loggerValue("exting from if loop inside for each loop ", "");
			}
		}

		return fees;
	}

	@Override
	public ArrayList<Profile> gotoloop(ArrayList<Profile> childList, Map<String, Profile> profiles)
			throws GSmartServiceException {

		Loggers.loggerStart(childList);

		if (childList.get(0).getRole().toLowerCase().equals("student")) {
			Map<String, Profile> profileMap = new HashMap<String, Profile>();
			fees = studentFees(childList);
			employeeProfile = profiledao.getProfileDetails(childList.get(0).getReportingManagerId());
			profileMap.put(employeeProfile.getSmartId(), employeeProfile);
			reportingSumUpfees = totalfees(profileMap, fees);
		}

		else {
			for (Profile profile : childList) {

				childOfChild = searchEmployeeInfo(profile.getSmartId(), profiles);
				for (Profile profile2 : childOfChild) {
					if (profile2.getRole().toLowerCase().equals("student")) {
						Map<String, Profile> profileMap = new HashMap<String, Profile>();

						fees = studentFees(childOfChild);
						employeeProfile = profiledao.getProfileDetails(childOfChild.get(0).getReportingManagerId());
						profileMap.put(employeeProfile.getSmartId(), employeeProfile);
						reportingSumUpfees = totalfees(profileMap, fees);
					} else {
						if (!childOfChild.isEmpty())
							gotoloop(childOfChild, profiles);
					}

				}

			}
		}
		Loggers.loggerValue("child of child list", reportingSumUpfees);
		Loggers.loggerEnd(reportingSumUpfees);
		return reportingSumUpfees;
	}

	public ArrayList<Profile> totalfees(Map<String, Profile> profileMap, ArrayList<Profile> fees) {

		Loggers.loggerStart(fees);
		Loggers.loggerValue("profile map", profileMap);
		for (Profile profile : fees) {
			/*
			 * if(!profileMap.get(profile).getReportingManagerId().isEmpty()) {
			 */

			profileMap.get(profile.getReportingManagerId()).setPaidAmount(
					profileMap.get(profile.getReportingManagerId()).getPaidAmount() + profile.getPaidAmount());

			profileMap.get(profile.getReportingManagerId()).setBalanceAmount(
					profileMap.get(profile.getReportingManagerId()).getBalanceAmount() + profile.getBalanceAmount());

			profileMap.get(profile.getReportingManagerId()).setTotalAmount(
					profileMap.get(profile.getReportingManagerId()).getTotalAmount() + profile.getTotalAmount());
			/*
			 * } else {
			 * 
			 * }
			 */

		}
		ArrayList<Profile> list = new ArrayList<Profile>(profileMap.values());
		allChilds.addAll(list);
		Loggers.loggerEnd(list);
		return list;

	}
	

	@Override
	public Map<String, Object> getParentInfo(String smartId) {
		Map<String, Object> parentInfo = new HashMap<>();
		Loggers.loggerStart();
		Profile parentProfile = profiledao.getParentInfo(smartId);
		parentInfo.put("parentProfile", parentProfile);
		if (parentProfile != null) {
			String parentSmartId = parentProfile.getSmartId();
			parentInfo.put("reportingProfiles", profiledao.getReportingProfiles(parentSmartId));
		} else
			parentInfo.put("reportingProfiles", null);
		return parentInfo;
	}

}
