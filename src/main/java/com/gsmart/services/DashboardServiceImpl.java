package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.DashboardDao;
import com.gsmart.model.Attendance;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService{
	
	@Autowired 
	private DashboardDao dashDao;

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
