package com.gsmart.dao;

import java.util.Map;

import com.gsmart.model.CompoundSalaryStructure;
import com.gsmart.model.SalaryStructure;
import com.gsmart.util.GSmartDatabaseException;

public interface SalaryStructureDAO {
	
	
	public Map<String, Object> getSalaryStructure(Long hid, Integer min, Integer max) throws GSmartDatabaseException;
 
	public CompoundSalaryStructure addSalaryStructure(SalaryStructure salarystructure) throws GSmartDatabaseException;
	
	public SalaryStructure editSalaryStructure(SalaryStructure salarystructure) throws GSmartDatabaseException;
	
}
