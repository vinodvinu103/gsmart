package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

@SuppressWarnings("deprecation")
@Entity
@Table(name="FEE")
public class Fee {
	
	
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
	
	@Column(name="ISACTIVE")
	private String isActive;
	
	
	
	public String getIsActive() {
		return isActive;
	}

	@Override
	public String toString() {
		return "Fee [smartId=" + smartId + ", entryTime=" + entryTime + ", academicYear=" + academicYear + ", name="
				+ name + ", isActive=" + isActive + ", UpdatedTime=" + UpdatedTime + ", ExitTime=" + ExitTime
				+ ", parentName=" + parentName + ", date=" + date + ", standard=" + standard + ", modeOfPayment="
				+ modeOfPayment + ", sportsFee=" + sportsFee + ", tuitionFee=" + tuitionFee + ", idCardFee=" + idCardFee
				+ ", miscellaneousFee=" + miscellaneousFee + ", transportationFee=" + transportationFee + ", hierarchy="
				+ hierarchy + ", balanceFee=" + balanceFee + ", paidFee=" + paidFee + ", totalFee=" + totalFee
				+ ", feeStatus=" + feeStatus + ", reportingManagerId=" + reportingManagerId + ", childFlag=" + childFlag
				+ ", parentFlag=" + parentFlag + "]";
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		UpdatedTime = updatedTime;
	}

	@Column(name="UPDATEDTIME")
	private String UpdatedTime;
	
	@Column(name="EXITTIME")
	private String ExitTime;
	
	public String getExitTime() {
		return ExitTime;
	}

	public void setExitTime(String exitTime) {
		ExitTime = exitTime;
	}



	@Column(name="PARENT_NAME")
	private String parentName;
	
	@Column(name="DATE")
	private String date;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="MODE_OF_PAYMENT")
	private String modeOfPayment;
	
	@Column(name="SPORTS_FEE")
	private Integer sportsFee;
	
	@Column(name="TUITION_FEE")
	private Integer tuitionFee;
	
	@Column(name="ID_CARD_FEE")
	private Integer idCardFee;
	
	@Column(name="MISCELLANEOUS_FEE")
	private Integer miscellaneousFee;
	
	@Column(name = "TRANSPORTATION_FEE")
	private String transportationFee;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}
	
	public String getTransportationFee() {
		return transportationFee;
	}

	public void setTransportationFee(String transportationFee) {
		this.transportationFee = transportationFee;
	}

	

	@Column(name="BALANCE_FEE")
	private Integer balanceFee;
	
	@Column(name="PAID_FEE")
	private Integer paidFee;
	
	@Column(name="TOATL_FEE")
	private Integer totalFee;
	
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

	public Integer getSportsFee() {
		return sportsFee;
	}

	public void setSportsFee(Integer sportsFee) {
		this.sportsFee = sportsFee;
	}

	public Integer getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(Integer tuitionFee) {
		this.tuitionFee = tuitionFee;
	}

	public Integer getIdCardFee() {
		return idCardFee;
	}

	public void setIdCardFee(Integer idCardFee) {
		this.idCardFee = idCardFee;
	}

	public Integer getMiscellaneousFee() {
		return miscellaneousFee;
	}

	public void setMiscellaneousFee(Integer miscellaneousFee) {
		this.miscellaneousFee = miscellaneousFee;
	}

	public Integer getBalanceFee() {
		return balanceFee;
	}

	public void setBalanceFee(Integer balanceFee) {
		this.balanceFee = balanceFee;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
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

	public Integer getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(Integer paidFee) {
		this.paidFee = paidFee;
	}

	

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
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
