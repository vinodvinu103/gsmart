package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TIME_TABLE")
@IdClass(com.gsmart.model.CompoundTimeTable.class)
public class TimeTable {

	@Column(name="ACADEMIC_YEAR")
	private String academicYear;
	
	@Id
	@Column(name="STANDARD")
	private String standard;
	
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Id
	@Column(name="SECTION")
	private String section;
	
	@Id
	@Column(name="DAY")
	private String day;
	
	@Id
	@Column(name="TIME_DURATION")
	private String time;
	
	@Column(name="SUBJECT")
	private String subject;
	
	@Column(name="SMART_ID")
	private String smartId;
	
	@Column(name="TEACHER_NAME")
	private String teacherName;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="UPDATE_TIME")
	private String updateTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="ROLE")
	private String role;
	
	@Column(name="PERIOD")
	private int period;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
	
	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "TimeTable [academicYear=" + academicYear + ", standard=" + standard + ", entryTime=" + entryTime
				+ ", section=" + section + ", day=" + day + ", time=" + time + ", subject=" + subject + ", smartId="
				+ smartId + ", teacherName=" + teacherName + ", isActive=" + isActive + ", updateTime=" + updateTime
				+ ", exitTime=" + exitTime + ", role=" + role + ", period=" + period + ", hierarchy=" + hierarchy + "]";
	}
	
	
	
}
