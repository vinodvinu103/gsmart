package com.gsmart.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.ReportCard;
import com.gsmart.util.GSmartDatabaseException;

public interface ReportCardDao {

	public List<ReportCard> reportCardList() throws GSmartDatabaseException;
	
	public CompoundReportCard addReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public ReportCard editReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public void deleteReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public void excelToDB(String smartId,MultipartFile fileUpload) throws Exception;
	
	public List<ReportCard> search(String smartId)throws GSmartDatabaseException;
	
	public List<ReportCard> search(String subject,int standard)throws GSmartDatabaseException;
	
	public List<ReportCard> searchBasedOnStandard(ReportCard card)throws GSmartDatabaseException;
}
