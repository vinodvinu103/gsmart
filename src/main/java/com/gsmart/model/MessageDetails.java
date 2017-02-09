package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@Table(name="MESSAGE_DETAILS")
@IdClass(com.gsmart.model.CompoundMessageDetails.class)
public class MessageDetails
{
	@Id
	@Column(name = "SMART_ID")
	String smartId;
	
	@Id
	@Column(name = "ENTRY_TIME")
	String entryTime;
	
	@Column(name="REPORTING_MANAGER_ID")
	String reportingManagerId;
	
	@Column(name="READBY_TEACHER")
	String readByTeacher;
	
	@Transient
	int childFlag;
	
	@Column(name = "POSTED_BY")
	String postedBy;
	
	@Lob
	@Column(name = "MESSAGE",length=512)
	String message;
	
	@Column(name= "READBY_STUDENT")
	String readByStudent;
	
	@Column(name="STUDENT_NAME")
	String studentName;
	
	@Column(name = "IMAGE", length = 400000)

	private byte[] image;
	
	
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

	public String getEntryTime() {
		return entryTime;
	}

	public int getChildFlag() {
		return childFlag;
	}

	

	public void setChildFlag(int childFlag) {
		this.childFlag = childFlag;
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
	
	
}