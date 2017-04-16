package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.SalaryStructureDAO;
import com.gsmart.model.SalaryStructure;

@Service
@Transactional
public class SalaryStructureServiceImpl implements SalaryStructureService{

	@Autowired
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

}
