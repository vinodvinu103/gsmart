package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
@SuppressWarnings("serial")
@Entity
@Table(name="WEEKDAYS")
@IdClass(com.gsmart.model.CompoundWeekDays.class)

public class WeekDays implements Serializable{
	
	
	//school name
	@Id
	@Column(name = "SCHOOL")
	private String school;
	
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

	//institute name
	@Id
	@Column(name = "INSTITUTION")
	private String institution;
	
	
@Id
@Column(name="WEEKDAY")
private String weekDay;

@Column(name="ISACTIVE")
private String isActive;



public String getIsActive() {
	return isActive;
}

public void setIsActive(String isActive) {
	this.isActive = isActive;
}

public String getWeekDay() {
	return weekDay;
}

public void setWeekDay(String weekDay) {
	this.weekDay = weekDay;
}
//school and institute purpose







}
