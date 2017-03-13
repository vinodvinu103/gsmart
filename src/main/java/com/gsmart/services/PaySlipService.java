package com.gsmart.services;

import com.gsmart.model.PaySlip;

public interface PaySlipService {

	void download(PaySlip paySlip);

	void adminDownload(PaySlip paySlip);

	void sendEmail(PaySlip paySlip);

}
