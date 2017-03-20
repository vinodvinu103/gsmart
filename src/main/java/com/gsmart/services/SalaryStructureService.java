package com.gsmart.services;

import java.util.List;

import com.gsmart.model.SalaryStructure;

public interface SalaryStructureService {

	List<SalaryStructure> view();

	void add(SalaryStructure salaryStructure);

	void edit(SalaryStructure salaryStructure);

	void delete(SalaryStructure salaryStructure);

}
