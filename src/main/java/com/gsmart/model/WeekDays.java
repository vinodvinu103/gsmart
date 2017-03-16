package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@SuppressWarnings("serial")
@Entity
@Table(name="WEEKDAYS")
@IdClass(com.gsmart.model.CompoundWeekDays.class)

public class WeekDays implements Serializable{
	
	
	@Id
	@Column(name = "SCHOOL")
	private String school;
	
	
		@Id
		@Column(name = "INSTITUTION")
		private String institution;
		
		@Id
		@Column(name="WEEKDAY")
		private String weekDay;

		@Column(name="ISACTIVE")
		private String isActive;

		@OneToOne(fetch=FetchType.EAGER)
		@JoinColumn(name="hid")
		private Hierarchy hierarchy;
		
		@Id
		@Column(name="ENTRY_TIME")
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

@Override
public String toString() {
	return "WeekDays [school=" + school + ", institution=" + institution + ", weekDay=" + weekDay + ", isActive="
			+ isActive + "]";
}


		public Hierarchy getHierarchy() {
			return hierarchy;
		}


		public void setHierarchy(Hierarchy hierarchy) {
			this.hierarchy = hierarchy;
		}


		@Override
		public String toString() {
			return "WeekDays [school=" + school + ", institution=" + institution + ", weekDay=" + weekDay
					+ ", isActive=" + isActive + ", hierarchy=" + hierarchy + "]";
		}

}
