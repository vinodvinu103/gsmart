package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface ReportCardDao {

	public List<ReportCard> reportCardList() throws GSmartDatabaseException;
	
	public CompoundReportCard addReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public ReportCard editReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public void deleteReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public void excelToDB(String smartId,MultipartFile fileUpload) throws Exception;
	
	public List<ReportCard> search(Token tokenDetail,String academicYear,String examName)throws GSmartDatabaseException;
	
	public ArrayList<ReportCard> reportCardBasedOnAcademicYear(String academicYear)throws GSmartDatabaseException;
	
	public List<ReportCard> acdemicYearAndExamName(Token tokenDetail)throws GSmartDatabaseException;
	
	public ArrayList<ReportCard> examName(Token tokenDetail,String acdemicYear)throws GSmartDatabaseException;
}
