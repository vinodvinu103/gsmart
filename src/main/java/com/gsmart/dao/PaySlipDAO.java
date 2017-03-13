package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.GenerateSalaryStatement;
import com.gsmart.model.PaySlip;
import com.gsmart.model.Profile;

public interface PaySlipDAO {

	GenerateSalaryStatement download(PaySlip paySlip);

	List<GenerateSalaryStatement> adminDownload(PaySlip paySlip);

	List<Profile> emailAddress(PaySlip paySlip);

}
