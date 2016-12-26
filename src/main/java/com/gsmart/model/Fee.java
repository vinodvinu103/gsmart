package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="FEE")
public class Fee {
	
	
	@Column(name="SMART_ID")
	private String smartId;
	
	@Id
	@Column(name="TIME_STAMP")
	private String timeStamp;
	
	@Column(name="ACADEMIC_YEAR")
	private String academicYear;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="PARENT_NAME")
	private String parentName;
	
	@Column(name="DATE")
	private String date;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="MODE_OF_PAYMENT")
	private String modeOfPayment;
	
	@Column(name="SPORTS_FEE")
	private int sportsFee;
	
	@Column(name="TUITION_FEE")
	private int tuitionFee;
	
	@Column(name="ID_CARD_FEE")
	private int idCardFee;
	
	@Column(name="MISCELLANEOUS_FEE")
	private int miscellaneousFee;
	
	@Column(name="BALANCE_FEE")
	private int balanceFee;
	
	@Column(name="PAID_FEE")
	private int paidFee;
	
	@Column(name="TOATL_FEE")
	private int totalFee;
	
	@Column(name="FEE_STATUS")
	private String feeStatus;
	
	@Column(name="REPORTING_MANAGER_ID")
	private String reportingManagerId;
	
	@Transient
	private boolean childFlag;
	
	@Transient
	private boolean parentFlag;
	

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public int getSportsFee() {
		return sportsFee;
	}

	public void setSportsFee(int sportsFee) {
		this.sportsFee = sportsFee;
	}

	public int getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(int tuitionFee) {
		this.tuitionFee = tuitionFee;
	}

	public int getIdCardFee() {
		return idCardFee;
	}

	public void setIdCardFee(int idCardFee) {
		this.idCardFee = idCardFee;
	}

	public int getMiscellaneousFee() {
		return miscellaneousFee;
	}

	public void setMiscellaneousFee(int miscellaneousFee) {
		this.miscellaneousFee = miscellaneousFee;
	}

	public int getBalanceFee() {
		return balanceFee;
	}

	public void setBalanceFee(int balanceFee) {
		this.balanceFee = balanceFee;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getFeeStatus() {
		return feeStatus;
	}

	public void setFeeStatus(String feeStatus) {
		this.feeStatus = feeStatus;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public int getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(int paidFee) {
		this.paidFee = paidFee;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public boolean isChildFlag() {
		return childFlag;
	}

	public void setChildFlag(boolean childFlag) {
		this.childFlag = childFlag;
	}

	public boolean isParentFlag() {
		return parentFlag;
	}

	public void setParentFlag(boolean parentFlag) {
		this.parentFlag = parentFlag;
	}

}
