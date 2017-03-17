package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.SalaryStructure;

public interface SalaryStructureDAO {

	List<SalaryStructure> view();

	void add(SalaryStructure salaryStructure);

	void edit(SalaryStructure salaryStructure);

	void delete(SalaryStructure salaryStructure);

}
