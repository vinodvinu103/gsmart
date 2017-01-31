package com.gsmart.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.BandDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Band;
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
	BandDao bandDao;
	
	@Autowired
	FeeMasterServices feeMasterServices;

	/*
	 * @RequestMapping(value = "/searchRep", method = RequestMethod.POST) public
	 * ResponseEntity<Map<String, ArrayList<Profile>>> searchRep(@RequestBody
	 * Search search) {
	 * 
	 * Map<String, ArrayList<Profile>> jsonMap = new HashMap<String,
	 * ArrayList<Profile>>(); Map<String, Profile> map =
	 * searchService.searchRep(search); ArrayList<Profile> profiless =
	 * searchService.getEmployeeInfo(search.getName(), map);
	 * jsonMap.put("result", profiless); return new ResponseEntity<Map<String,
	 * ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
	 * 
	 * }
	 */

	private Map<String, Profile> allProfiles;

	@Override
	public Map<String, Profile> getAllProfiles(String academicYear) {
		Loggers.loggerStart();
		allProfiles = new HashMap<String, Profile>();
		List<Profile> profiles = profiledao.getAllRecord(academicYear);
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
				if (emp != null) {
					if ((p.getTeacherId().trim().toLowerCase()).startsWith(emp.toLowerCase())) {
						list.add(p);
					}
				} else {
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
		Set<String> key = map.keySet();
		ArrayList<Profile> childList = new ArrayList<Profile>();

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

	/*
	 * @Override public List<Notice> searchSpecificNotice(ArrayList<String>
	 * parentList, List<Notice> notices) {
	 * 
	 * logger.info(parentList.size()); logger.info(notices.size()); List<Notice>
	 * noticeList = new ArrayList<Notice>();
	 * 
	 * for (int i = 0; i < parentList.size(); i++) { for (int j = 0; j <
	 * notices.size(); j++) { Notice x = notices.get(j);
	 * logger.info(parentList.get(i)); logger.info(x.getSmart_id()); if
	 * (x.getSmart_id().equals(parentList.get(i))) { noticeList.add(x); } } }
	 * return noticeList; }
	 */

	@Override
	public ArrayList<Profile> sumUpFee(ArrayList<Profile> childList, Map<String, Profile> profiles,String academicYear)
			throws GSmartServiceException {

		Loggers.loggerStart();
		ArrayList<Profile> fees = new ArrayList<Profile>();

		int i = 0;

		boolean boo = false;

		Map<Integer, ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>();

		Map<String, Profile> profileMap = new HashMap<String, Profile>();

		ArrayList<Profile> temp1 = new ArrayList<Profile>();

		ArrayList<Profile> temp2 = new ArrayList<Profile>();

		map.put(++i, childList);

		temp1 = childList;

		if (!childList.isEmpty()) {
			Loggers.loggerValue("if childList is not empty", "");


			if (childList.get(0).getRole().toLowerCase().equals("student")) {

				fees = studentFees(childList,academicYear);

				return fees;

			} 
			else {
				do {

					ArrayList<Profile> gotoloop = gotoloop(temp1, profiles);
					if (!gotoloop.isEmpty()) {

						map.put(++i, gotoloop);


						boo = map.get(i).get(0).getRole().toLowerCase().equals("student");

						temp1 = map.get(i);
						Loggers.loggerValue("temp1 value ", temp1);


						if (boo) {
							fees = studentFees(temp1,academicYear);
						}
					} else {
						Loggers.loggerValue("sumup ended", "");
						return childList;
					}

				} while (!boo);
				
			}
				

				do {

					for (int j = 0; j < map.get(i - 1).size(); j++) {

						profileMap.put(map.get(i - 1).get(j).getSmartId(), map.get(i - 1).get(j));

					}


					Collections.sort(fees, new Comparator<Profile>() {
						public int compare(Profile s1, Profile s2) {
							// Write your logic here.
							if (s1.getBand() > s2.getBand()) {
								return 1;
							} else if(s1.getBand() == s2.getBand()){
								return 0;
							} else {
								return -1;
							}
						}
					});
					
					Band band=bandDao.getMaxband();
					
					if(band.getRole().toLowerCase().equals("student")) {
						Collections.reverse(fees);
					}



					temp2 = totalfees(profileMap, fees);

					fees = temp2;

					i--;

				} while (i > 1);

				return temp2;
			
			
			}
		else {
			return childList;
		}
		
	}

	@Override
	public ArrayList<Profile> studentFees(ArrayList<Profile> childList,String academicYear) throws GSmartServiceException {

		Loggers.loggerStart(childList);
		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Fee> feeList = feeServices.getFeeLists(academicYear);

		ArrayList<FeeMaster> fee =  (ArrayList<FeeMaster>) feeMasterServices.getFeeList();

		Map<String, Fee> feeMap = new HashMap<String, Fee>();

		Map<String, Integer> feeMasterMap = new HashMap<String, Integer>();


		for (int i = 0; i < feeList.size(); i++) {

			feeMap.put(feeList.get(i).getSmartId(), feeList.get(i));
		}

		for (int i = 0; i < fee.size(); i++) {

			feeMasterMap.put(fee.get(i).getStandard(), fee.get(i).getTotalFee());


		}

		for (Profile profile : childList) {


			if (feeMap.get(profile.getSmartId()) != null) {

				profile.setPaidAmount(feeMap.get(profile.getSmartId()).getPaidFee());
				profile.setBalanceAmount(feeMap.get(profile.getSmartId()).getBalanceFee());
				profile.setTotalAmount(feeMasterMap.get(profile.getStandard()));

				fees.add(profile);
			} else {
				profile.setTotalAmount(feeMasterMap.get(profile.getStandard()));

				fees.add(profile);
			}
		}

		return fees;
	}

	@Override
	public ArrayList<Profile> gotoloop(ArrayList<Profile> childList, Map<String, Profile> profiles)
			throws GSmartServiceException {

		ArrayList<Profile> childOfChildList = new ArrayList<>();
		Loggers.loggerStart(childList);
		ArrayList<Profile> childOfChild = new ArrayList<Profile>();

		for (Profile profile : childList) {

			childOfChild = searchEmployeeInfo(profile.getSmartId(), profiles);
			if (!childOfChild.isEmpty()) {
				for (Profile childProfile : childOfChild) {
					System.out.println("child of child");
					childOfChildList.add(childProfile);
				}
			}

		}
		Loggers.loggerValue("child of child list", childOfChildList);
		Loggers.loggerEnd();
		return childOfChildList;
	}

	public ArrayList<Profile> totalfees(Map<String, Profile> profileMap, ArrayList<Profile> fees) {

		Loggers.loggerStart(fees);
		Loggers.loggerValue("profile map", profileMap);
		for (Profile profile : fees) {

			profileMap.get(profile.getReportingManagerId()).setPaidAmount(
					profileMap.get(profile.getReportingManagerId()).getPaidAmount() + profile.getPaidAmount());


			profileMap.get(profile.getReportingManagerId()).setBalanceAmount(
					profileMap.get(profile.getReportingManagerId()).getBalanceAmount() + profile.getBalanceAmount());


			profileMap.get(profile.getReportingManagerId()).setTotalAmount(
					profileMap.get(profile.getReportingManagerId()).getTotalAmount() + profile.getTotalAmount());


		}
		ArrayList<Profile> list = new ArrayList<Profile>(profileMap.values());
		Loggers.loggerEnd(list);
		return list;

	}


	public Profile totalFessToAdmin(Profile profileMap, ArrayList<Profile> fees) {
		Loggers.loggerStart();
		for (Profile profile : fees) {
			if (profileMap.getSmartId().equals(profile.getReportingManagerId())) {
				profileMap.setPaidAmount(profileMap.getPaidAmount() + profile.getPaidAmount());
				profileMap.setBalanceAmount(profileMap.getBalanceAmount() + profile.getBalanceAmount());
				profileMap.setTotalAmount(profileMap.getTotalAmount() + profile.getTotalAmount());
			}
		}
		Loggers.loggerEnd(profileMap);
		return profileMap;

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
