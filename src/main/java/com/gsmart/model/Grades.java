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
@Table(name="GRADES")
@IdClass(com.gsmart.model.CompoundGrades.class)
public class Grades {

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
	
	@Id
	@Column(name="SCHOOL")
    private String school;
	
	@Id
	@Column(name="INSTITUTION")
	private String institution;
	
	@Column(name="START_PERCENTAGE")
	private float startPercentage;
	
	@Column(name="END_PERCENTAGE")
	private float endPercentage;
	
	@Id
	@Column(name="GRADE")
	private String grade;
	
	@Column(name="ISACTIVE")
	private String isActive;

	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="UPDATE_TIME")
	private String updateTime;




	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public float getStartPercentage() {
		return startPercentage;
	}

	public void setStartPercentage(float startPercentage) {
		this.startPercentage = startPercentage;
	}

	public float getEndPercentage() {
		return endPercentage;
	}

	public void setEndPercentage(float endPercentage) {
		this.endPercentage = endPercentage;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Grades [hierarchy=" + hierarchy + ", school=" + school + ", institution=" + institution
				+ ", startPercentage=" + startPercentage + ", endPercentage=" + endPercentage + ", grade=" + grade
				+ ", isActive=" + isActive + ", entryTime=" + entryTime + ", exitTime=" + exitTime + ", updateTime="
				+ updateTime + "]";
	}
	
}
