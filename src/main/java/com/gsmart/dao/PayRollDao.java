package com.gsmart.dao;

import java.util.Map;

import com.gsmart.model.PayRoll;
import com.gsmart.util.GSmartDatabaseException;

public interface PayRollDao {
	public Map<String, Object> getPayroll() throws GSmartDatabaseException;
	public PayRoll addPayroll(PayRoll payroll) throws GSmartDatabaseException;
	public PayRoll editPayroll(PayRoll payroll) throws GSmartDatabaseException;
	public void deletePayroll(PayRoll payroll) throws GSmartDatabaseException;

}
