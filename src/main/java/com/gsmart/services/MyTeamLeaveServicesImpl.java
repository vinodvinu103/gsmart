package com.gsmart.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.MyTeamLeaveDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.LeaveDetails;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class MyTeamLeaveServicesImpl implements MyTeamLeaveServices {
	@Autowired
	MyTeamLeaveDao myteamleaveDao;
	
	@Override
	public List<Leave> getLeavelist(Profile profileInfo,Hierarchy hierarchy) throws GSmartServiceException{
		Loggers.loggerStart();
		List<Leave> list = null;
		try {
		list= myteamleaveDao.getLeavelist(profileInfo,hierarchy);
		/*for (Leave leave : list) {
			
			switch(leave.getLeaveStatus()){
			case "Sanction":
				leave.setbutton
			}*/
			/*case "Band":
				rolePermission.setIcon("white fa fa-bitcoin fa-3x");
				break;
			case "Holiday":
				rolePermission.setIcon("white fa fa-calendar fa-3x");
				break;*/
			
		} catch (GSmartDatabaseException exception) {
//			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		return list;
	}
	
	@Override
	public void rejectleave(Leave leave) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			myteamleaveDao.rejectleave(leave);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

	@Override
	public void sactionleave(Leave leave) throws GSmartServiceException {
		Loggers.loggerStart();
		
		try {
			myteamleaveDao.sactionleave(leave);
			//LeaveDetails ld=(LeaveDetails)Session.get(LeaveDetails.class);
			LeaveDetails s=new LeaveDetails();
			s.setAppliedLeaves(leave.getNumberOfDays());
			s.setLeaveType(leave.getLeaveType());
			s.setLeftLeaves(20);
			
			
			
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}
	
	@Override
	public void cancelSanctionLeave(Leave leave) throws GSmartServiceException {
Loggers.loggerStart();
		
		try {
			myteamleaveDao.cancelSanctionLeave(leave);
		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
		 
	}

}
