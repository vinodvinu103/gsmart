package com.gsmart.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.HolidayDao;
import com.gsmart.dao.LeaveDao;
import com.gsmart.dao.LeaveMasterDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.WeekDaysDao;
import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Holiday;
import com.gsmart.model.Leave;
import com.gsmart.model.LeaveMaster;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.model.WeekDays;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class LeaveServicesImpl implements LeaveServices {

	@Autowired
	private LeaveDao leaveDao;

	@Autowired
	HolidayDao holidayDao;

	@Autowired
	WeekDaysDao weekDays;

	@Autowired
	ProfileDao profileDao;

	@Autowired
	LeaveMasterDao leaveMasterDao;

	@Override
	public Map<String, Object> getLeaveList(Token tokenObj, Hierarchy hierarchy, int min, int max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return leaveDao.getLeaveList(tokenObj, hierarchy, min, max);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
			throw new GSmartServiceException(e.getMessage());
		}
	}

	public void editLeave(Leave leave) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			leaveDao.editLeave(leave);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
			throw new GSmartServiceException(e.getMessage());

		}
	}

	@Override
	public CompoundLeave addLeave(Leave leave, Integer noOfdays, String smartId, String role, Hierarchy hierarchy)
			throws GSmartServiceException {
		Loggers.loggerStart();
		Profile profile = null;
		profile = profileDao.getProfileDetails(smartId);
		String school = profile.getSchool();
		String institution = profile.getInstitution();
		CompoundLeave cl = null;
		try {
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();

			startCal.setTime(leave.getStartDate());
			endCal.setTime(leave.getEndDate());
			if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
				Loggers.loggerEnd();
				return cl = null;
			}
			endCal.add(Calendar.DAY_OF_MONTH, 1);

			Calendar holidayDate = Calendar.getInstance();

			long diff = endCal.getTimeInMillis() - startCal.getTimeInMillis();
			int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			
			cl=avoidLeaveForSameDate(leave, hierarchy.getHid());
			if(cl==null){
				ArrayList<WeekDays> weekOffs = (ArrayList<WeekDays>) weekDays.getWeekdaysForHoliday(school, institution);

				for (WeekDays weekDays : weekOffs) {
				System.out.println("Weekdays " + weekDays.getWeekDay());
				}
				System.out.println("Weekoffs size: " + weekOffs.size());
				endCal.setTime(leave.getEndDate());
				for (int i = 0; i < weekOffs.size(); i++) {
					while (startCal.compareTo(endCal) <= 0) {

						System.out.println("bweek: " + i + " : " + startCal.get(Calendar.DAY_OF_MONTH) + " : "
							+ weekOffs.get(i).getWeekDay());
						if (startCal.get(Calendar.DAY_OF_WEEK) == Integer.parseInt(weekOffs.get(i).getWeekDay())) {
						System.out.println("aweek: " + i + " : " + startCal.get(Calendar.DAY_OF_MONTH) + " : "
								+ weekOffs.get(i).getWeekDay());
						days--;
						}

						startCal.add(Calendar.DAY_OF_MONTH, 1);
					}

					startCal.setTime(leave.getStartDate());
				} // for

				System.out.println("days: " + days);

				List<Holiday> list = holidayDao.holidayList(hierarchy.getHid());

				long eStartDate = getEpoch(leave.getStartDate());
				long eEndDate = getEpoch(leave.getEndDate());
				for (Holiday holiday : list) {
					System.out.println("############ in side foreachloop " + holiday.getHolidayDate());
					long holiDate = getEpoch(holiday.getHolidayDate());
					holidayDate.setTime(holiday.getHolidayDate());
					if ((eStartDate <= holiDate) && (holiDate <= eEndDate)) {
						System.out.println("in side if condition  $$$$$$$$$$$$$$$");
						days--;
						System.out.println("redused work days*************  " + days);
						for (int i = 0; i < weekOffs.size(); i++) {
						System.out.println("b holidaayDate: " + i + " : " + holidayDate.get(Calendar.DAY_OF_MONTH)
								+ " : " + weekOffs.get(i).getWeekDay());
						if (holidayDate.get(Calendar.DAY_OF_WEEK) == Integer.parseInt(weekOffs.get(i).getWeekDay())) {
							System.out.println("after holidaayDate: " + i + " : "
									+ holidayDate.get(Calendar.DAY_OF_MONTH) + " : " + weekOffs.get(i).getWeekDay());
							days++;
						}
						System.out.println("out of if loop and days : " + days);
					}

				}

			}

			Loggers.loggerEnd();
			cl = leaveDao.addLeave(leave, days);
			}
			else{
				cl = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cl;
	}// compound leave
	
	public int getWorkingDaysBetweenTwoDates(Calendar startCal, Calendar endCal)
			throws ParseException, GSmartDatabaseException {
		Loggers.loggerStart();
		int workDays = 0;
		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			Calendar temp = Calendar.getInstance();
			temp = startCal;
			startCal = endCal;
			endCal = temp;
		}

		while (startCal.getTimeInMillis() < endCal.getTimeInMillis()) {
			// excluding start date
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
			++workDays;

		Loggers.loggerEnd();
		return workDays;
	}

	@Override
	public void deleteLeave(Leave leave) throws GSmartServiceException {
		Loggers.loggerStart();
		try {

			leaveDao.deleteLeave(leave);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
			throw new GSmartServiceException(e.getMessage());

		} // catch
		Loggers.loggerEnd();

	}// delete

	public long getEpoch(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date date2 = calendar.getTime();
		long epoch = date2.getTime() / 1000;
		System.out.println("saakappa e timeformate " + epoch);
		return epoch;
	}// epoch

	public Map<String, Object> getLeftLeaves(String role, Hierarchy hierarchy, String smartId, String leaveType) {
		int totalleaves = 0;
		int leftLeaves = 0;
		Map<String, Object> map = new HashMap<>();
		LeaveMaster leaveTypeData = leaveMasterDao.getLeaveMasterByType(role, hierarchy, leaveType);
		List<Leave> leaveList = leaveDao.getLeaves(role, hierarchy, smartId, leaveType);
		for (int i = 0; i < leaveList.size(); i++) {
			totalleaves = leaveList.get(i).getNumberOfDays() + totalleaves;
		}
		leftLeaves = leaveTypeData.getDaysAllow() - totalleaves;
		map.put("totalLeaves", totalleaves);
		map.put("leftLeaves", leftLeaves);

		return map;

	}

	public CompoundLeave avoidLeaveForSameDate(Leave leave , Long hid) throws GSmartDatabaseException{
		Loggers.loggerStart();
		CompoundLeave cpl=null;
		List<Leave> listForAdd=leaveDao.leaveForAdding(leave, hid);
		long aS=getEpoch(leave.getStartDate());
		long aE=getEpoch(leave.getEndDate());
		
		
		for (Leave leave2 : listForAdd) {
			long dS=getEpoch(leave2.getStartDate());
			long dE=getEpoch(leave2.getEndDate());
			if(dS<=aS && aE<=dE){
				cpl=new CompoundLeave();
				cpl.setSmartId("");
				break;
			}
			else if (dS<=aS && aS<=dE) {
				cpl=new CompoundLeave();
				cpl.setSmartId("");
				break;
			}
			else if (dS<=aE && aE<=dE) {
				cpl=new CompoundLeave();
				cpl.setSmartId("");
				break;
			}
			else if (aS<=dS && dE<=aE) {
				cpl=new CompoundLeave();
				cpl.setSmartId("");
				break;
			}
			else{
				cpl=null;
			}
		}	
		Loggers.loggerEnd();
		return cpl;
	}
}
