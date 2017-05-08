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
@Table(name = "LEAVE_DETAILS")
@IdClass(com.gsmart.model.CompoundLeaveDetails.class)
public class LeaveDetails {

	@Id
	@Column(name = "SMART_ID")
//	@Index(name = "smartId")
	private String smartId;

	@Id
	@Column(name = "LEAVE_TYPE")
//	@Index(name = "leaveType")
	private String leaveType;
	
	@Column(name = "APPLIED_LEAVES")
	private int appliedLeaves;

	@Column(name = "LEFT_LEAVES")
	private int leftLeaves;

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public int getAppliedLeaves() {
		return appliedLeaves;
	}

	public void setAppliedLeaves(int appliedLeaves) {
		this.appliedLeaves = appliedLeaves;
	}

	public int getLeftLeaves() {
		return leftLeaves;
	}

	public void setLeftLeaves(int leftLeaves) {
		this.leftLeaves = leftLeaves;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

}
