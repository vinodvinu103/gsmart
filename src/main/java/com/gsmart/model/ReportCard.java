package com.gsmart.model;



/*import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;*/
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
/*import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;*/
import javax.persistence.Table;

@Entity
@Table(name="REPORT_CARD")
@IdClass(com.gsmart.model.CompoundReportCard.class)
public class ReportCard {

	@Id
	@Column(name="SMARTID")
	private String smartId;
	
	@Id
	@Column(name="ENTRYTIME")
	private String entryTime;
	
	@Column(name="STUDENT_NAME")
	private String studentName;
	
	@Column(name="STANDARD")
	private int standard;
	
	@Column(name="SECTION")
	private String section;
	
	/*@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="STUDENT_SUBJECT", joinColumns={@JoinColumn(name="SMARTID"),@JoinColumn(name="ENTRYTIME")},
		inverseJoinColumns={@JoinColumn(name="SUBJECT_ID")})
	private Set<Subject> subjects=new HashSet<Subject>(0);*/
	
	
	@Column(name="TEACHER_NAME")
	private String teacherName;
	
	@Column(name="SUBJECT")
	private String subject;
	
	@Column(name="MAX_MARKS")
	private int maxMarks;
	
	@Column(name="MIN_MARKS")
	private int minMarks;
	
	@Column(name="MARKS_OBTAINED")
	private int marksObtained;
	
	@Column(name="SUBJECT_GRADE")
	private String subjectGrade;
	
	@Column(name="TOTAL_GRADE")
	private String totalGrade;
	
	@Column(name="RESULT")
	private String result;
	
	@Column(name="ACADAMIC_YEAR")
	private String acadamicYear;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="UPDATE_TIME")
	private String updateTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getStandard() {
		return standard;
	}

	public void setStandard(int standard) {
		this.standard = standard;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getMaxMarks() {
		return maxMarks;
	}

	public void setMaxMarks(int maxMarks) {
		this.maxMarks = maxMarks;
	}

	public int getMinMarks() {
		return minMarks;
	}

	public void setMinMarks(int minMarks) {
		this.minMarks = minMarks;
	}

	public int getMarksObtained() {
		return marksObtained;
	}

	public void setMarksObtained(int marksObtained) {
		this.marksObtained = marksObtained;
	}

	public String getSubjectGrade() {
		return subjectGrade;
	}

	public void setSubjectGrade(String subjectGrade) {
		this.subjectGrade = subjectGrade;
	}

	public String getTotalGrade() {
		return totalGrade;
	}

	public void setTotalGrade(String totalGrade) {
		this.totalGrade = totalGrade;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}
	

	public String getAcadamicYear() {
		return acadamicYear;
	}

	public void setAcadamicYear(String acadamicYear) {
		this.acadamicYear = acadamicYear;
	}

	@Override
	public String toString() {
		return "ReportCard [smartId=" + smartId + ", entryTime=" + entryTime + ", studentName=" + studentName
				+ ", standard=" + standard + ", section=" + section + ", teacherName="
				+ teacherName + ", subject=" + subject + ", maxMarks=" + maxMarks + ", minMarks="
				+ minMarks + ", marksObtained=" + marksObtained + ", subjectGrade=" + subjectGrade + ", totalGrade="
				+ totalGrade + ", result=" + result + ", acadamicYear=" + acadamicYear + ", isActive=" + isActive
				+ ", updateTime=" + updateTime + ", exitTime=" + exitTime + "]";
	}

	
	
	
	
	
}