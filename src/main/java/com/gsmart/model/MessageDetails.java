package com.gsmart.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.print.DocFlavor.INPUT_STREAM;

import org.hibernate.annotations.Index;



@SuppressWarnings("deprecation")
@Entity
@Table(name="MESSAGE_DETAILS")
@IdClass(com.gsmart.model.CompoundMessageDetails.class)
public class MessageDetails
{
	@Id
	@Column(name = "SMART_ID")
	@Index(name = "smartId")
	String smartId;
	
	@Column(name = "ENTRY_TIME")
	String entryTime;

	@Column(name="REPORTING_MANAGER_ID")
	String reportingManagerId;
	
	@Column(name="READBY_TEACHER")
	String readByTeacher;
	
	@Transient
	int childFlag;
	
	@Column(name = "POSTED_BY")
	@Index(name = "postedBy")
	String postedBy;
	
	@Column(name = "POSTED_TO")
	String postedTo;
	
	@Column(name = "ACADEMIC_YEAR")
	private String academicYear;
	
	@Column(name = "CLASS_SECTION")
	private String classsection;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	@Index(name = "hierarchy")
	private Hierarchy hierarchy;

	@Id
	@Lob
	@Column(name = "MESSAGE",length=512)
	String message;
	
	@Column(name= "READBY_STUDENT")
	String readByStudent;
	
	@Column(name="STUDENT_NAME")
	String studentName;
	
	@Column(name="FORMAT")
	String format;
	
	@Column(name="STUDENT_ID")
	String studentId;
	
	@Column(name = "IMAGE", length = 400000)
    @Lob
	private byte[] image;
//    private INPUT_STREAM image;
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}


	public int getChildFlag() {
		return childFlag;
	}

	public void setChildFlag(int childFlag) {
		this.childFlag = childFlag;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getReadByTeacher() {
		return readByTeacher;
	}

	public void setReadByTeacher(String readByTeacher) {
		this.readByTeacher = readByTeacher;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}
	
	public String getPostedTo() {
		return postedTo;
	}

	public void setPostedTo(String postedTo) {
		this.postedTo = postedTo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReadByStudent() {
		return readByStudent;
	}

	public void setReadByStudent(String readByStudent) {
		this.readByStudent = readByStudent;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getClasssection() {
		return classsection;
	}

	public void setClasssection(String classsection) {
		this.classsection = classsection;
	}

	@Override
	public String toString() {
		return "MessageDetails [smartId=" + smartId + ", entryTime=" + entryTime + ", reportingManagerId="
				+ reportingManagerId + ", readByTeacher=" + readByTeacher + ", childFlag=" + childFlag + ", postedBy="
				+ postedBy + ", postedTo=" + postedTo + ", academicYear=" + academicYear + ", classsection="
				+ classsection + ", hierarchy=" + hierarchy + ", message=" + message + ", readByStudent="
				+ readByStudent + ", studentName=" + studentName + ", format=" + format + ", studentId=" + studentId
				+ ", image=" + Arrays.toString(image) + "]";
	}
	
	
}