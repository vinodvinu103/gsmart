package com.gsmart.services;

import java.util.Map;

import com.gsmart.model.CompoundSalaryStructure;
import com.gsmart.model.SalaryStructure;
import com.gsmart.util.GSmartServiceException;

public interface SalaryStructureService {

	public Map<String, Object> getSalaryStructure(Long hid, Integer min, Integer max) throws GSmartServiceException;
	 
	public CompoundSalaryStructure addSalaryStructure(SalaryStructure salarystructure) throws GSmartServiceException ;
	
	public SalaryStructure editSalaryStructure(SalaryStructure salarystructure) throws GSmartServiceException;
	

}
