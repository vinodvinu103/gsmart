package com.gsmart.services;

import java.util.Map;

import com.gsmart.model.PayRoll;
import com.gsmart.util.GSmartServiceException;

public interface PayRollService {
	
	public Map<String, Object> getPayroll() throws GSmartServiceException;
	public PayRoll addPayroll(PayRoll payroll) throws GSmartServiceException;
	public PayRoll editPayroll(PayRoll payroll) throws GSmartServiceException;
	public void deletePayroll(PayRoll payroll) throws GSmartServiceException;
}
