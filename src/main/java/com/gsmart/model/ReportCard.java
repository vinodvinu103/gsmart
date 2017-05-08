package com.gsmart.model;



/*import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;*/
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
/*import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;*/
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("deprecation")
@Entity
@Table(name="REPORT_CARD")
@IdClass(com.gsmart.model.CompoundReportCard.class)
public class ReportCard {

	@Id
	@Column(name="SMARTID")
//	@Index(name = "smartId")
	private String smartId;
	
	@Id
	@Column(name="ENTRYTIME")
	private String entryTime;
	
	@Id
	@Column(name="EXAM_NAME")
	private String examName;
	
	@Column(name="STUDENT_NAME")
	private String studentName;
	
	@Column(name="STUDENT_ID")
	private String studentId;
	
	@Column(name="STANDARD")
//	@Index(name = "standard")
	private String standard;
	
	@Column(name="SECTION")
	private String section;
	
	@Column(name="REPORTING_MANAGER_ID")
	private String reportingManagerId;
	
	@Column(name="TEACHER_NAME")
	private String teacherName;
	
	@Id
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
	
	@Transient
	private String totalGrade;
	
	@Column(name="RESULT")
	private String result;
	
	@Column(name="ACADEMIC_YEAR")
//	@Index(name = "academicYear")
	private String academicYear;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="UPDATE_TIME")
	private String updateTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Transient
	private int childReportFlag;
	
	@Column(name="REMARK")
	private String remark;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

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
	
	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
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
	

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public int getChildReportFlag() {
		return childReportFlag;
	}

	public void setChildReportFlag(int childReportFlag) {
		this.childReportFlag = childReportFlag;
	}
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString() {
		return "ReportCard [smartId=" + smartId + ", entryTime=" + entryTime + ", examName=" + examName
				+ ", studentName=" + studentName + ", studentId=" + studentId + ", standard=" + standard + ", section="
				+ section + ", reportingManagerId=" + reportingManagerId + ", teacherName=" + teacherName + ", subject="
				+ subject + ", maxMarks=" + maxMarks + ", minMarks=" + minMarks + ", marksObtained=" + marksObtained
				+ ", subjectGrade=" + subjectGrade + ", academicYear=" + academicYear + ", isActive=" + isActive
				+ ", hierarchy=" + hierarchy + "]";
	}
	
	
	
	
}
