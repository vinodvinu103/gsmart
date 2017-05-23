package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface ReportCardDao {

	public List<ReportCard> reportCardList(Token tokenDetail) throws GSmartDatabaseException;
	
	public CompoundReportCard addReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public ReportCard editReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public void deleteReportCard(ReportCard card)throws GSmartDatabaseException;
	
	public void excelToDB(String smartId,MultipartFile fileUpload,Hierarchy hid) throws Exception;
	
	public List<ReportCard> search(Token tokenDetail,String academicYear,String examName)throws GSmartDatabaseException;
	
	public ArrayList<ReportCard> reportCardforHOD(Token token, String examName, String academicYear,String smartId)throws GSmartDatabaseException;
	
	public List<ReportCard> acdemicYearAndExamName(Token tokenDetail)throws GSmartDatabaseException;
	
	public ArrayList<ReportCard> examName(Token tokenDetail,String acdemicYear,String smartId)throws GSmartDatabaseException;

	public Map<String, Object> reportCardListForTeacher(Token tokenDetail,Integer min,Integer max,String acdemicYear,String examName) throws GSmartDatabaseException;

	public ArrayList<Profile> findChildTeacher(Token tokenObj, String acdemicYear)throws GSmartDatabaseException;
	
	public List<Profile> studentList(Token tokenDetail) throws GSmartDatabaseException;
	
	public Fee studentFee(String smartId, String academicYear)throws GSmartDatabaseException;
}
