package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@SuppressWarnings("deprecation")
@Entity
@Table(name = "WEEKDAYS")
@IdClass(com.gsmart.model.CompoundWeekDays.class)

public class WeekDays {

	@Id
	@Column(name = "WEEK_DAY")
	private String weekDay;

	@Column(name = "ISACTIVE")
//	@Index(name = "isActive")
	private String isActive;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hid")
//	@Index(name = "hierarchy")
	private Hierarchy hierarchy;

	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

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

	@Override
	public String toString() {
		return "WeekDays [ weekDay=" + weekDay + ", isActive=" + isActive + ", hierarchy=" + hierarchy + "]";
	}

}