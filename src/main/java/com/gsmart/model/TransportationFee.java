package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@SuppressWarnings("deprecation")
@Entity
@Table(name="TRANSPORTATION")
public class TransportationFee {
	
	@Column(name="SMART_ID")
	@Index(name = "smartId")
	private String smartId;
	
	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;
	
	@Column(name="ACADEMIC_YEAR")
	@Index(name = "academicYear")
	private String academicYear;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name = "DISTANCE")
	private String distance;
	

	@Column(name = "SECTION")
	private String section;


	@Column(name="ISACTIVE")
	private String isActive;
	
	@Column(name="IN_VOICE")
	private String inVoice;
	
	@Column(name="UPDATEDTIME")
	private String UpdatedTime;
	
	@Column(name="EXITTIME")
	private String ExitTime;
	
	@Column(name="PARENT_NAME")
	private String parentName;
	
	@Column(name="DATE")
	private String date;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="MODE_OF_PAYMENT")
	private String modeOfPayment;
	
	@Column(name="CHEQUE_NUMBER")
	private Integer chequeNumber;
	
	@Column(name="BANK_NAME")
	private String bankName;
	
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
	
	@Column(name="BALANCE_FEE")
	private Integer balanceFee;
	
	@Column(name="PAID_FEE")
	private Integer paidFee;
	
	@Column(name="TOATL_FEE")
	private Integer totalTransportationFee;
	
	@Column(name="FEE_STATUS")
	private String feeStatus;
	
	@Column(name="REPORTING_MANAGER_ID")
	private String reportingManagerId;
	

	@Column(name="")
	private String feeType;
	

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	

	public Integer getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(Integer paidFee) {
		this.paidFee = paidFee;
	}
	
	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getInVoice() {
		return inVoice;
	}

	public void setInVoice(String inVoice) {
		this.inVoice = inVoice;
	}

	public String getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		UpdatedTime = updatedTime;
	}

	public String getExitTime() {
		return ExitTime;
	}

	public void setExitTime(String exitTime) {
		ExitTime = exitTime;
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

	

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Integer getBalanceFee() {
		return balanceFee;
	}

	public void setBalanceFee(Integer balanceFee) {
		this.balanceFee = balanceFee;
	}

	

	


	public Integer getTotalTransportationFee() {
		return totalTransportationFee;
	}

	public void setTotalTransportationFee(Integer totalTransportationFee) {
		this.totalTransportationFee = totalTransportationFee;
	}

	public String getFeeStatus() {
		return feeStatus;
	}

	public void setFeeStatus(String feeStatus) {
		this.feeStatus = feeStatus;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getDistance() {
		return distance;
	}

	public Integer getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(Integer chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "TransportationFee [smartId=" + smartId + ", entryTime=" + entryTime + ", academicYear=" + academicYear
				+ ", name=" + name + ", distance=" + distance + ", section=" + section + ", isActive=" + isActive
				+ ", inVoice=" + inVoice + ", UpdatedTime=" + UpdatedTime + ", ExitTime=" + ExitTime + ", parentName="
				+ parentName + ", date=" + date + ", standard=" + standard + ", modeOfPayment=" + modeOfPayment
				+ ", chequeNumber=" + chequeNumber + ", bankName=" + bankName + ", hierarchy=" + hierarchy
				+ ", balanceFee=" + balanceFee + ", paidFee=" + paidFee + ", totalTransportationFee="
				+ totalTransportationFee + ", feeStatus=" + feeStatus + ", reportingManagerId=" + reportingManagerId
				+ ", feeType=" + feeType + "]";
	}

	
	

}
