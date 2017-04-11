package com.gsmart.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.SalaryStructureDAO;
import com.gsmart.model.CompoundSalaryStructure;
import com.gsmart.model.SalaryStructure;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class SalaryStructureServiceImpl implements SalaryStructureService{

	@Autowired
	private SalaryStructureDAO salarystructuredao;
	
	@Override
	public Map<String, Object> getSalaryStructure(Long hid, Integer min, Integer max) throws GSmartServiceException {
		salarystructuredao.getSalaryStructure(hid, min, max);
		return null;
	}

	@Override
	public CompoundSalaryStructure addSalaryStructure(SalaryStructure salarystructure) throws GSmartServiceException {
		Loggers.loggerStart();
		CompoundSalaryStructure compoundSalaryStructure=null;
		try{
			compoundSalaryStructure=salarystructuredao.addSalaryStructure(salarystructure);
		}
		catch (Exception e) {
		e.printStackTrace();
		}
		Loggers.loggerEnd();
		return compoundSalaryStructure;		
	}

	@Override
	public SalaryStructure editSalaryStructure(SalaryStructure salarystructure) throws GSmartServiceException {
		Loggers.loggerStart();
		SalaryStructure salaryStructure = null;
		try{
			salaryStructure =salarystructuredao.editSalaryStructure(salaryStructure);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return salaryStructure;		
	}
	
	
	

	/*@Autowired
	private SalaryStructureDAO salaryStructureDAO;
	
	@Override
	public List<SalaryStructure> view() {
	
		return salaryStructureDAO.view();
	}

	@Override
	public void add(SalaryStructure salaryStructure) {

		salaryStructureDAO.add(salaryStructure);
	}

	@Override
	public void edit(SalaryStructure salaryStructure) {

		salaryStructureDAO.edit(salaryStructure);
	}

	@Override
	public void delete(SalaryStructure salaryStructure) {

		salaryStructureDAO.delete(salaryStructure);
	}
*/
}
