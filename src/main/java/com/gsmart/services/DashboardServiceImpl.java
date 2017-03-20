package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.DashboardDao;
import com.gsmart.model.Attendance;

@Service
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired DashboardDao dashDao;

	@Override
	public List<Attendance> getAttendance() {
		
		return dashDao.getAttendance();
	}

	/*@Override
	public int getInventory() {
		
		return dashDao.getInventory();
	}
	@Override
	public int getTotalfee(){
		
		return dashDao.getTotalfee();
	}*/

}
