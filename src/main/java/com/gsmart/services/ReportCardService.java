package com.gsmart.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.ReportCard;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface ReportCardService {
	
	
	public List<ReportCard> reportCardList() throws GSmartServiceException;
	
	public CompoundReportCard addReportCard(ReportCard card)throws GSmartServiceException;
	
	public ReportCard editReportCard(ReportCard card)throws GSmartServiceException;
	
	public void deleteReportCard(ReportCard card)throws GSmartServiceException;
	
	public void excelToDB(String smartId,MultipartFile fileUpload) throws Exception;
	
	public List<ReportCard> search(String smartId)throws GSmartServiceException;
	
	public List<ReportCard> search(String subject, int standard)throws GSmartServiceException;
	
	public List<ReportCard> searchBasedOnStandard(ReportCard card)throws GSmartServiceException;
}
