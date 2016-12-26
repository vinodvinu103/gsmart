package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
//import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.util.GSmartServiceException;

@Service
public class SearchServiceImp implements SearchService {

	@Autowired
	ProfileDao profiledao;

	@Autowired
	FeeServices feeServices;

	@Autowired/*@RequestMapping(value = "/searchRep", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<Profile>>> searchRep(@RequestBody Search search) {
	
	Map<String, ArrayList<Profile>> jsonMap = new HashMap<String, ArrayList<Profile>>();
		Map<String, Profile> map = searchService.searchRep(search);
		ArrayList<Profile> profiless = searchService.getEmployeeInfo(search.getName(), map);
		jsonMap.put("result", profiless);
		return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
	
}	*/
	FeeMasterServices feeMasterServices;

	private static final Logger logger = Logger.getLogger(SearchServiceImp.class);
	private Map<String, Profile> allProfiles;


	@Override
	public Map<String, Profile> getAllProfiles() {
		allProfiles = new HashMap<String, Profile>();
		List<Profile> profiles = profiledao.getAllRecord();
		for (Profile profile : profiles) {
			allProfiles.put(profile.getSmartId(), profile);
		}
		return allProfiles;
	}

	@Override
	public ArrayList<Profile> getEmployeeInfo(String emp, Map<String, Profile> map) {
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
		return list;
	}

	@Override
	public ArrayList<Profile> searchEmployeeInfo(String smartId, Map<String, Profile> map) {
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
		return childList;
	}

	public Map<String, Profile> searchRep(Search search) {

		allProfiles = new HashMap<String, Profile>();

		List<Profile> profiles = profiledao.getsearchRep(search);

		logger.info(profiles.size());
		for (Profile profile : profiles) {
			logger.info(profiles.size());
			logger.info(profile.getSmartId());
			allProfiles.put(profile.getSmartId(), profile);
		}
		return allProfiles;
	}

	@Override
	public ArrayList<String> searchParentInfo(String smartId, Map<String, Profile> map) {

		ArrayList<String> parentList = new ArrayList<String>();

		parentList.add(smartId);

		Profile p;
		boolean temp;
		do {
			logger.info(smartId);
			p = map.get(smartId);
			if (!(p.getReportingManagerId().equals(smartId))) {
				parentList.add(p.getReportingManagerId());
				smartId = p.getReportingManagerId();
				temp = true;
				logger.info(smartId);
			} else {
				temp = false;
			}
		} while (temp);
		logger.info(parentList.size());
		return parentList;
	}

	/*@Override
	public List<Notice> searchSpecificNotice(ArrayList<String> parentList, List<Notice> notices) {

		logger.info(parentList.size());
		logger.info(notices.size());
		List<Notice> noticeList = new ArrayList<Notice>();

		for (int i = 0; i < parentList.size(); i++) {
			for (int j = 0; j < notices.size(); j++) {
				Notice x = notices.get(j);
				logger.info(parentList.get(i));
				logger.info(x.getSmart_id());
				if (x.getSmart_id().equals(parentList.get(i))) {
					noticeList.add(x);
				}
			}
		}
		return noticeList;
	}*/

	@Override
	public ArrayList<Profile> sumUpFee(ArrayList<Profile> childList, Map<String, Profile> profiles)
			throws GSmartServiceException {

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

			if (childList.get(0).getRole().toLowerCase().equals("student")) {

				fees = studentFees(childList);

				return fees;

			} else {
				do {

					map.put(++i, gotoloop(temp1, profiles));

					boo = map.get(i).get(0).getRole().toLowerCase().equals("student");

					temp1 = map.get(i);

					if (boo) {
						fees = studentFees(temp1);
					}

				} while (!boo);

				do {

					for (int j = 0; j < map.get(i - 1).size(); j++) {

						profileMap.put(map.get(i - 1).get(j).getSmartId(), map.get(i - 1).get(j));

					}

					temp2 = totalfees(profileMap, fees);

					fees = temp2;

					i--;

				} while (i > 1);

				return temp2;
			}
		}

		else {
			return childList;
		}

	}

	@Override
	public ArrayList<Profile> studentFees(ArrayList<Profile> childList) throws GSmartServiceException {

		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Fee> feeList = feeServices.getFeeLists("2016-2017");
		
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

		ArrayList<Profile> childOfChild = new ArrayList<Profile>();
		
		for (Profile profile : childList) {

			childOfChild = searchEmployeeInfo(profile.getSmartId(), profiles);

		}
		
		return childOfChild;
	}

	public ArrayList<Profile> totalfees(Map<String, Profile> profileMap, ArrayList<Profile> fees) {

		for (Profile profile : fees) {

			profileMap.get(profile.getReportingManagerId()).setPaidAmount(
					profileMap.get(profile.getReportingManagerId()).getPaidAmount() + profile.getPaidAmount());

			profileMap.get(profile.getReportingManagerId()).setBalanceAmount(
					profileMap.get(profile.getReportingManagerId()).getBalanceAmount() + profile.getBalanceAmount());


			profileMap.get(profile.getReportingManagerId()).setTotalAmount(
					profileMap.get(profile.getReportingManagerId()).getTotalAmount() + profile.getTotalAmount());

		}
		ArrayList<Profile> list = new ArrayList<Profile>(profileMap.values());
		return list;

	}
	
	
	
	

}
