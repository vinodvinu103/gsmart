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
@Table(name = "LEAVEMASTER")
@IdClass(com.gsmart.model.CompoundLeaveMaster.class)
public class LeaveMaster {
	@Id
	@Column(name = "LEAVE_TYPE")
	@Index(name = "leaveType")
	private String leaveType;
    @Id
	@Column(name = "DAYS_ALLOW")
	private int daysAllow;
    @Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;

	@Column(name = "UPDATE_TIME")
	private String updateTime;

	@Column(name = "EXIT_TIME")
	private String exitTime;

	@Column(name = "IS_ACTIVE")
	@Index(name = "isActive")
	private String isActive;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	@Index(name = "hierarchy")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public int getDaysAllow() {
		return daysAllow;
	}

	public void setDaysAllow(int daysAllow) {
		this.daysAllow = daysAllow;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "\n LeaveMaster [\n\t leaveType=" + leaveType + ", \n\tdaysAllow=" + daysAllow + ", \n\tentryTime="
				+ entryTime + ", \n\tupdateTime=" + updateTime + ", \n\texitTime=" + exitTime + ", \n\tisActive=" + isActive + "]";
	}



}
