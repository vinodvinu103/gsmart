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
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.BandDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Band;
import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Hierarchy;
//import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class SearchServiceImp implements SearchService {

	@Autowired
	private ProfileDao profiledao;

	@Autowired
	private FeeServices feeServices;

	@Autowired
	private BandDao bandDao;

	@Autowired
	private FeeMasterServices feeMasterServices;

	private Map<String, Profile> allProfiles;

	@Override
	public Map<String, Profile> getAllProfiles(String academicYear, Long hid) {
		Loggers.loggerStart();
		allProfiles = new HashMap<String, Profile>();
		List<Profile> profiles = profiledao.getAllRecord(academicYear, hid);
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

				if ((p.getSmartId().trim().toLowerCase().startsWith(emp.toLowerCase()))) {
					list.add(p);
					// }else if(p.getFirstName().trim().toLowerCase().startsWith(emp.toLowerCase()))
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
		ArrayList<Profile> childList = new ArrayList<Profile>();
		/* Loggers.loggerValue("profiles in map", map); */
		try {
			Set<String> key = map.keySet();

			/* Loggers.loggerValue("key", key); */

			for (String temp : key) {
				Profile p = map.get(temp);
				if (p.getReportingManagerId() != null && p.getReportingManagerId().equals(smartId)) {

					System.out.println("reporting manager id" + p.getReportingManagerId());
					System.out.println("smart id" + smartId);
					if (!(p.getSmartId().equals(smartId))) {
						childList.add(p);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(childList);
		return childList;
	}

	/*
	 * @Override public ArrayList<Profile> searchEmployeeInfoForFinance(String
	 * smartId, Map<String, Profile> map) {
	 * Loggers.loggerStart("searchEmployeeInfo "); ArrayList<Profile> childList =
	 * new ArrayList<Profile>(); Loggers.loggerValue("profiles in map", map); try {
	 * Set<String> key = map.keySet();
	 * 
	 * Loggers.loggerValue("key", key);
	 * 
	 * for (String temp : key) { Profile p = map.get(temp); if
	 * (p.getFinanceManagerId() != null) { if
	 * (p.getFinanceManagerId().equals(smartId)) { if
	 * (!(p.getSmartId().equals(smartId))) { childList.add(p); } } }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); }
	 * Loggers.loggerEnd(childList); return childList; }
	 * 
	 * @Override public ArrayList<Profile> searchEmployeeInfoForHr(String smartId,
	 * Map<String, Profile> map) { Loggers.loggerStart("searchEmployeeInfo ");
	 * ArrayList<Profile> childList = new ArrayList<Profile>();
	 * Loggers.loggerValue("profiles in map", map); try { Set<String> key =
	 * map.keySet();
	 * 
	 * Loggers.loggerValue("key", key);
	 * 
	 * for (String temp : key) { Profile p = map.get(temp); if (p.getHrManagerId()
	 * != null) { if (p.getHrManagerId().equals(smartId)) { if
	 * (!(p.getSmartId().equals(smartId))) { childList.add(p); } } }
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); }
	 * Loggers.loggerEnd(childList); return childList; }
	 */

	public Map<String, Profile> searchRep(Search search, String role, Hierarchy hierarchy) {

		Loggers.loggerStart();
		allProfiles = new HashMap<String, Profile>();

		List<Profile> profiles = profiledao.getsearchRep(search, role, hierarchy);

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
		Loggers.loggerStart("searchParentInfo Api started in Search Service for id :" + smartId);
		ArrayList<String> parentList = new ArrayList<String>();

		parentList.add(smartId);

		Profile p;
		boolean temp;
		do {
			p = map.get(smartId);
			if (p != null && p.getReportingManagerId() != null && !(p.getReportingManagerId().equals(smartId))) {
					parentList.add(p.getReportingManagerId());
					smartId = p.getReportingManagerId();
					temp = true;
			} else {
				temp = false;
			}
		} while (temp);

		Set<String> keys = map.keySet();
		for (String smartid : keys) {
			if (map.get(smartid).getRole() != null && map.get(smartid).getRole().equalsIgnoreCase("hr")) {

				parentList.add(smartid);

			}

		}
		Loggers.loggerEnd("searchParentInfo Api ended in Search Service for id :" + smartId
				+ " with parentList size of " + parentList.size());
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
	 * (x.getSmart_id().equals(parentList.get(i))) { noticeList.add(x); } } } return
	 * noticeList; }
	 */

	@Override
	public ArrayList<Profile> sumUpFee(ArrayList<Profile> childList, Map<String, Profile> profiles, String academicYear,
			Long hid) throws GSmartServiceException {

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

			if (childList.get(0).getRole().equalsIgnoreCase("student")) {
				fees = studentFees(childList, academicYear, hid, 0, 20);

				return fees;

			} else {
				do {
					ArrayList<Profile> gotoloop = gotoloop(temp1, profiles);
					if (!gotoloop.isEmpty()) {

						map.put(++i, gotoloop);

						boo = map.get(i).get(0).getRole().equalsIgnoreCase("student");

						temp1 = map.get(i);
						Loggers.loggerValue("temp1 value ", temp1);

						if (boo) {
							fees = studentFees(temp1, academicYear, hid, 0, 20);
						}
						/*
						 * }else { Loggers.loggerValue("sumup ended", ""); return childList; }
						 */

					} else {
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
						} else if (s1.getBand() == s2.getBand()) {
							return 0;
						} else {
							return -1;
						}
					}
				});

				Band band = bandDao.getMaxband();

				if (band.getRole().equalsIgnoreCase("student")) {
					Collections.reverse(fees);
				}

				temp2 = totalfees(profileMap, fees);

				fees = temp2;

				i--;

			} while (i > 1);

			return temp2;

		} else {
			return childList;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> studentFees(ArrayList<Profile> childList, String academicYear, Long hid, int min, int max)
			throws GSmartServiceException {

		Loggers.loggerStart(childList);
		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Fee> feeList = feeServices.getFeeLists(academicYear, hid);

		System.out.println("min" + min + "/t" + "max" + max + "/t" + "hid" + hid);

		System.out.println("feeList" + feeList);

		Map<String, Object> fee = (Map<String, Object>) feeMasterServices.getFeeList(hid, min, max);

		Map<String, Fee> feeMap = new HashMap<String, Fee>();

		Map<String, Integer> feeMasterMap = new HashMap<String, Integer>();

		for (int i = 0; i < feeList.size(); i++) {
			Loggers.loggerStart(feeList.size());
			feeMap.put(feeList.get(i).getSmartId(), feeList.get(i));
		}

		List<FeeMaster> feeMaster = (List<FeeMaster>) fee.get("feeList");
		for (int i = 0; i < feeMaster.size(); i++) {
			Loggers.loggerStart(feeMaster.size());

			feeMasterMap.put((feeMaster.get(i)).getStandard(), (feeMaster.get(i)).getTotalFee());

		}

		for (Profile profile : childList) {

			if (feeMap.get(profile.getSmartId()) != null) {

				profile.setPaidAmount(feeMap.get(profile.getSmartId()).getPaidFee());
				profile.setBalanceAmount(feeMap.get(profile.getSmartId()).getBalanceFee());
				profile.setTotalAmount(feeMasterMap.get(profile.getStandard()));

				fees.add(profile);
			} else {
				profile.setTotalAmount(feeMasterMap.get(profile.getStandard()));
				profile.setBalanceAmount(feeMasterMap.get(profile.getStandard()));

				fees.add(profile);
			}
		}

		Loggers.loggerEnd();
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

	public ArrayList<Profile> childOfChilds(ArrayList<Profile> childList, Map<String, Profile> profiles)
			throws GSmartServiceException {

		ArrayList<Profile> childOfChildList = new ArrayList<>();
		Loggers.loggerStart(childList);

		ArrayList<Profile> childOfChild = new ArrayList<Profile>();

		for (Profile profile : childList) {

			childOfChild = searchEmployeeInfo(profile.getSmartId(), profiles);
			/* if (!childOfChild.isEmpty()) { */
			for (Profile childProfile : childOfChild) {
				System.out.println("child of child");
				childOfChildList.add(childProfile);
			}
			/*
			 * }else{ return childList; }
			 */

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
			Loggers.loggerValue("total fees of set fees", "");

			profileMap.get(profile.getReportingManagerId()).setBalanceAmount(
					profileMap.get(profile.getReportingManagerId()).getBalanceAmount() + profile.getBalanceAmount());
			Loggers.loggerValue("total fees of setbalance fees", "");

			profileMap.get(profile.getReportingManagerId()).setTotalAmount(
					profileMap.get(profile.getReportingManagerId()).getTotalAmount() + profile.getTotalAmount());

			Loggers.loggerValue("total fees of settotal fees", "");

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
	public Map<String, Object> getParentInfo(String smartId) throws GSmartServiceException {
		Map<String, Object> parentInfo = new HashMap<>();
		Loggers.loggerStart();
		Profile parentProfile;
		try {
			parentProfile = profiledao.getParentInfo(smartId);
			parentInfo.put("parentProfile", parentProfile);
			if (parentProfile != null) {
				String parentSmartId = parentProfile.getSmartId();
				parentInfo.put("reportingProfiles", profiledao.getReportingProfiles(parentSmartId));
			} else
				parentInfo.put("reportingProfiles", null);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}

		return parentInfo;
	}

	@Override
	public ArrayList<String> getAllChildSmartId(String parentId, Map<String, Profile> allProfiles) {
		Loggers.loggerStart("getAllChildSmartId in services is called with parentId : " + parentId);
		ArrayList<String> childSmartIdList = new ArrayList<String>();
		ArrayList<Profile> childList = searchEmployeeInfo(parentId, allProfiles);
		ArrayList<Profile> tempProfile = childList;
		Map<Integer, ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>();
		int i = 1;
		map.put(i, childList);
		boolean boo = false;
		try {
			do {
				if (!tempProfile.isEmpty()) {
					ArrayList<Profile> gotoloop = childOfChilds(tempProfile, allProfiles);
					if (!gotoloop.isEmpty()) {
						map.put(++i, gotoloop);
						boo = map.get(i).get(0).getRole().equalsIgnoreCase("student");
						tempProfile = map.get(i);
						Loggers.loggerValue("tempProfile value ", tempProfile);
						for (Profile profile : gotoloop) {
							childSmartIdList.add(profile.getSmartId());
						}
					} else {
						for (Profile profile : childList) {
							childSmartIdList.add(profile.getSmartId());
						}
						return childSmartIdList;
					}
				} else {
					boo = true;
				}
			} while (!boo);
			for (Profile profile : childList) {
				childSmartIdList.add(profile.getSmartId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(
				"Total profiles reporting to the parentId : " + parentId + " is of size : " + childSmartIdList.size());
		return childSmartIdList;
	}

	/*
	 * @Override public ArrayList<String> getAllChildSmartIdForFinanceFee(String
	 * parentId, Map<String, Profile> allProfiles) { Loggers.
	 * loggerStart("getAllChildSmartId in services is called with parentId : " +
	 * parentId); ArrayList<String> childSmartIdList = new ArrayList<String>();
	 * ArrayList<Profile> childList = searchEmployeeInfoForFinance(parentId,
	 * allProfiles); ArrayList<Profile> tempProfile = childList; Map<Integer,
	 * ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>(); int i =
	 * 1; map.put(i, childList); boolean boo = false; try { do { if
	 * (!tempProfile.isEmpty()) { ArrayList<Profile> gotoloop =
	 * childOfChilds(tempProfile, allProfiles); if (!gotoloop.isEmpty()) {
	 * map.put(++i, gotoloop); boo =
	 * map.get(i).get(0).getRole().toLowerCase().equals("student"); tempProfile =
	 * map.get(i); Loggers.loggerValue("tempProfile value ", tempProfile); for
	 * (Profile profile : gotoloop) { childSmartIdList.add(profile.getSmartId()); }
	 * } else { for (Profile profile : childList) {
	 * childSmartIdList.add(profile.getSmartId()); } return childSmartIdList; } }
	 * else { boo = true; } } while (!boo); for (Profile profile : childList) {
	 * childSmartIdList.add(profile.getSmartId()); } } catch (Exception e) {
	 * e.printStackTrace(); } Loggers.loggerEnd(
	 * "Total profiles reporting to the parentId : " + parentId + " is of size : " +
	 * childSmartIdList.size()); return childSmartIdList; }
	 * 
	 * @Override public ArrayList<String> getAllChildSmartIdForHrFee(String
	 * parentId, Map<String, Profile> allProfiles) { Loggers.
	 * loggerStart("getAllChildSmartId in services is called with parentId : " +
	 * parentId); ArrayList<String> childSmartIdList = new ArrayList<String>();
	 * ArrayList<Profile> childList = searchEmployeeInfoForHr(parentId,
	 * allProfiles); ArrayList<Profile> tempProfile = childList; Map<Integer,
	 * ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>(); int i =
	 * 1; map.put(i, childList); boolean boo = false; try { do { if
	 * (!tempProfile.isEmpty()) { ArrayList<Profile> gotoloop =
	 * childOfChilds(tempProfile, allProfiles); if (!gotoloop.isEmpty()) {
	 * map.put(++i, gotoloop); boo =
	 * map.get(i).get(0).getRole().toLowerCase().equals("student"); tempProfile =
	 * map.get(i); Loggers.loggerValue("tempProfile value ", tempProfile); for
	 * (Profile profile : gotoloop) { childSmartIdList.add(profile.getSmartId()); }
	 * } else { for (Profile profile : childList) {
	 * childSmartIdList.add(profile.getSmartId()); } return childSmartIdList; } }
	 * else { boo = true; } } while (!boo); for (Profile profile : childList) {
	 * childSmartIdList.add(profile.getSmartId()); } } catch (Exception e) {
	 * e.printStackTrace(); } Loggers.loggerEnd(
	 * "Total profiles reporting to the parentId : " + parentId + " is of size : " +
	 * childSmartIdList.size()); return childSmartIdList; }
	 */

	@Override
	public ArrayList<Profile> getAllChildSmartIdForDashboard(String parentId, Map<String, Profile> allProfiles) {
		Loggers.loggerStart("getAllChildSmartId in services is called with parentId : " + parentId);
		ArrayList<Profile> childSmartIdList = new ArrayList<Profile>();
		ArrayList<Profile> childList = searchEmployeeInfo(parentId, allProfiles);
		ArrayList<Profile> tempProfile = childList;
		Map<Integer, ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>();
		int i = 1;
		map.put(i, childList);
		boolean boo = false;
		try {
			do {
				if (!tempProfile.isEmpty()) {
					ArrayList<Profile> gotoloop = childOfChilds(tempProfile, allProfiles);
					if (!gotoloop.isEmpty()) {
						map.put(++i, gotoloop);
						boo = map.get(i).get(0).getRole().toLowerCase().equals("student");
						tempProfile = map.get(i);
						Loggers.loggerValue("tempProfile value ", tempProfile);
						for (Profile profile : gotoloop) {
							childSmartIdList.add(profile);
						}
					} else {
						for (Profile profile : childList) {
							childSmartIdList.add(profile);
						}
						return childSmartIdList;
					}
				} else {
					boo = true;
				}
			} while (!boo);
			for (Profile profile : childList) {
				childSmartIdList.add(profile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(
				"Total profiles reporting to the parentId : " + parentId + " is of size : " + childSmartIdList.size());
		return childSmartIdList;
	}

	/*
	 * @Override public ArrayList<Profile>
	 * getAllChildSmartIdForFinanceAttendance(String parentId, Map<String, Profile>
	 * allProfiles) { Loggers.
	 * loggerStart("getAllChildSmartId in services is called with parentId : " +
	 * parentId); ArrayList<Profile> childSmartIdList = new ArrayList<Profile>();
	 * ArrayList<Profile> childList = searchEmployeeInfoForFinance(parentId,
	 * allProfiles); ArrayList<Profile> tempProfile = childList; Map<Integer,
	 * ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>(); int i =
	 * 1; map.put(i, childList); boolean boo = false; try { do { if
	 * (!tempProfile.isEmpty()) { ArrayList<Profile> gotoloop =
	 * childOfChilds(tempProfile, allProfiles); if (!gotoloop.isEmpty()) {
	 * map.put(++i, gotoloop); boo =
	 * map.get(i).get(0).getRole().toLowerCase().equals("student"); tempProfile =
	 * map.get(i); Loggers.loggerValue("tempProfile value ", tempProfile); for
	 * (Profile profile : gotoloop) { childSmartIdList.add(profile); } } else { for
	 * (Profile profile : childList) { childSmartIdList.add(profile); } return
	 * childSmartIdList; } } else { boo = true; } } while (!boo); for (Profile
	 * profile : childList) { childSmartIdList.add(profile); } } catch (Exception e)
	 * { e.printStackTrace(); } Loggers.loggerEnd(
	 * "Total profiles reporting to the parentId : " + parentId + " is of size : " +
	 * childSmartIdList.size()); return childSmartIdList; }
	 * 
	 * @Override public ArrayList<Profile> getAllChildSmartIdForHrAttendance(String
	 * parentId, Map<String, Profile> allProfiles) { Loggers.
	 * loggerStart("getAllChildSmartId in services is called with parentId : " +
	 * parentId); ArrayList<Profile> childSmartIdList = new ArrayList<Profile>();
	 * ArrayList<Profile> childList = searchEmployeeInfoForHr(parentId,
	 * allProfiles); ArrayList<Profile> tempProfile = childList; Map<Integer,
	 * ArrayList<Profile>> map = new HashMap<Integer, ArrayList<Profile>>(); int i =
	 * 1; map.put(i, childList); boolean boo = false; try { do { if
	 * (!tempProfile.isEmpty()) { ArrayList<Profile> gotoloop =
	 * childOfChilds(tempProfile, allProfiles); if (!gotoloop.isEmpty()) {
	 * map.put(++i, gotoloop); boo =
	 * map.get(i).get(0).getRole().toLowerCase().equals("student"); tempProfile =
	 * map.get(i); Loggers.loggerValue("tempProfile value ", tempProfile); for
	 * (Profile profile : gotoloop) { childSmartIdList.add(profile); } } else { for
	 * (Profile profile : childList) { childSmartIdList.add(profile); } return
	 * childSmartIdList; } } else { boo = true; } } while (!boo); for (Profile
	 * profile : childList) { childSmartIdList.add(profile); } } catch (Exception e)
	 * { e.printStackTrace(); } Loggers.loggerEnd(
	 * "Total profiles reporting to the parentId : " + parentId + " is of size : " +
	 * childSmartIdList.size()); return childSmartIdList; }
	 */

}
