package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;
import com.itextpdf.text.Document;

public interface ReportCardService {
	
	
	public List<ReportCard> reportCardList() throws GSmartServiceException;
	
	public CompoundReportCard addReportCard(ReportCard card)throws GSmartServiceException;
	
	public ReportCard editReportCard(ReportCard card)throws GSmartServiceException;
	
	public void deleteReportCard(ReportCard card)throws GSmartServiceException;
	
	public void excelToDB(String smartId,MultipartFile fileUpload) throws Exception;
	
	public List<ReportCard> search(Token tokenDetail,String academicYear,String examName)throws GSmartServiceException;
	
	public String calculatPercentage(String smartId, List<ReportCard> childReportCards)throws GSmartServiceException;

	public Document downloadPdf(Token tokenDetail,String academicYear,String examName)throws GSmartServiceException;

}
