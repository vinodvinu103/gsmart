package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name ="LEAVE_DETAILS")
@IdClass(com.gsmart.model.CompoundLeaveDetails.class)
public class LeaveDetails {

	@Id
	@Column(name="SMART_ID")
private String smartId;
	
	@Column(name="LEAVE_TYPE")
	private String leaveType;
	
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
	
    @Id
    @Column(name="APPLIED_LEAVES")
	private int appliedLeaves;
	
    @Column(name="LEFT_LEAVES")
	private int leftLeaves;
	

}
