package com.gsmart.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.HolidayDaoImpl;
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
	HolidayDaoImpl getholidaylist;

	@Autowired
	WeekDaysDao weekDays;
	
	@Autowired
	ProfileDao profileDao;
	
	@Autowired
	LeaveMasterDao leaveMasterDao;

	@Override
	public List<Leave> getLeaveList(Token tokenObj,Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return leaveDao.getLeaveList(tokenObj,hierarchy);
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
	public CompoundLeave addLeave(Leave leave,Integer noOfdays,String smartId,String role,Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		Profile profile=null;
		profile=profileDao.profileDetails(smartId);
		String school=profile.getSchool();
		String institution=profile.getInstitution();
		CompoundLeave cl=null;
		try {
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
		
			
			startCal.setTime(leave.getStartDate());
			endCal.setTime(leave.getEndDate());
			endCal.add(Calendar.DAY_OF_MONTH, 1);

			Calendar holidayDate = Calendar.getInstance();

			long diff = endCal.getTimeInMillis()- startCal.getTimeInMillis();
			int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

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
			}//for

			System.out.println("days: " + days);

			ArrayList<Holiday> list = (ArrayList<Holiday>) getholidaylist.getHolidayList(role,hierarchy);

			long eStartDate = getEpoch(leave.getStartDate());
			System.out.println("start date >>>>>>>......"+leave.getStartDate());
			long eEndDate = getEpoch(leave.getEndDate());
			System.out.println("end  date .......>>>>>>>"+leave.getEndDate());
			for (Holiday holiday : list) {
				System.out.println("############ in side foreachloop "+holiday.getHolidayDate());
				long holiDate = getEpoch(holiday.getHolidayDate());
				holidayDate.setTime(holiday.getHolidayDate());
				if ((eStartDate <= holiDate) && (holiDate <= eEndDate)) {
					System.out.println("in side if condition  $$$$$$$$$$$$$$$");
					days--;
					System.out.println("redused work days*************  " + days);
					for (int i = 0; i < weekOffs.size(); i++){
						System.out.println("b holidaayDate: " + i + " : " + holidayDate.get(Calendar.DAY_OF_MONTH) + " : "
								+ weekOffs.get(i).getWeekDay());
						if(holidayDate.get(Calendar.DAY_OF_WEEK)==Integer.parseInt(weekOffs.get(i).getWeekDay())){
							System.out.println("after holidaayDate: " + i + " : " + holidayDate.get(Calendar.DAY_OF_MONTH) + " : "
									+ weekOffs.get(i).getWeekDay());
							days++;
						}
						System.out.println("out of if loop and days : "+days);
					}
					
				}

			}

			Loggers.loggerEnd();
			cl = leaveDao.addLeave(leave, days);

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cl;
	}//compound leave

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

		}//catch
		Loggers.loggerEnd();

	}//delete
	
	public long getEpoch(Date date) {
		Calendar calendar = Calendar.getInstance();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
	/*	Date date1 = df.parse(date);*/
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date date2 = calendar.getTime();
		long epoch = date2.getTime() / 1000;
		//long epoch = date.getTime();
		System.out.println("saakappa e timeformate " + epoch);
		return epoch;
	}//epoch 
	
	public Map<String,Object> getLeftLeaves(String role,Hierarchy hierarchy,String smartId,String leaveType){
		int totalleaves=0;
		int leftLeaves=0;
		Map<String, Object> map=new HashMap<>();
		LeaveMaster leaveTypeData=leaveMasterDao.getLeaveMasterByType(role,hierarchy,leaveType);
		List<Leave> leaveList=leaveDao.getLeaves(role,hierarchy,smartId, leaveType);
		for(int i=0;i<leaveList.size();i++){
			totalleaves=leaveList.get(i).getNumberOfDays()+totalleaves;
		}
		leftLeaves=leaveTypeData.getDaysAllow()-totalleaves;
		map.put("totalLeaves", totalleaves);
		map.put("leftLeaves", leftLeaves);
		
		return map;
		
	}

}
