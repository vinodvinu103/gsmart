package com.gsmart.model;

import java.io.Serializable;


public class CompoundGrades implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String school;
	
	private String institution;
	
	private String grade;
	
	private String entryTime;

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
