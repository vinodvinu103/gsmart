package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@SuppressWarnings("deprecation")
@Entity
@Table(name="GENERATE_SALARY_STATEMENT")
@IdClass(CompoundGenerateSalaryStatement.class)
public class GenerateSalaryStatement {

	@Id
	@Column(name="SMART_ID")
	@Index(name = "smartId")
	private String smartId;
	
	@Id
	@Column(name="MONTH")
	private String month;
	
	@Id
	@Column(name="YEAR")
	private String year;

	@Id
	@Column(name="ENTRY_TIME")
	private String entryTime;

	@Column(name="EMP_NAME")
	@Index(name = "empName")
	private String empName;
	
	@Column(name="EMP_DESIGNATION")
	private String empDesignation;
	
	@Column(name="EMP_ROLE")
	@Index(name = "empRole")
	private String empRole;
	
	@Column(name="UPD_SMART_ID")
	private Integer updSmartId;
	
	@Column(name="BASIC_SALARY")
	private Double basicSalary;
	
	@Column(name="HRA")
	private Double hra;
	
	@Column(name="PF")
	private Double pf;
	
	@Column(name="PT")
	private Double pt;
	
	@Column(name="ESI")
	private Double esi;
	
	@Column(name="CONVEYANCE")
	private Double conveyance;
	
	@Column(name="IT")
	private Double it;
	
	
	
	@Column(name="ACTUAL_SALARY")
	private Double actualSalary;
	
	@Column(name="DEDUCTED_SALARY")
	private Double deductedSalary;
	
	@Column(name="PAYABLE")
	private Double payable;
	
	@Column(name="OT_AMOUNT")
	private Double otAmount;
	
	@Column(name="OT_ALLOWANCE")
	private Double otAllowance;
	
	@Column(name="SPECIAL_ALLOWANCE")
	private Double specialAllowance;
	
	@Column(name="NON_PAID_LEAVES_DAYS")
	private int nonPaidLeavesDays;
	
	@Column(name="NON_PAID_LEAVES_AMOUNT")
	private Double nonPaidLeavesAmount;

	

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpDesignation() {
		return empDesignation;
	}

	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}

	public Integer getUpdSmartId() {
		return updSmartId;
	}

	public void setUpdSmartId(Integer updSmartId) {
		this.updSmartId = updSmartId;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getHra() {
		return hra;
	}

	public void setHra(Double hra) {
		this.hra = hra;
	}

	public Double getPf() {
		return pf;
	}

	public void setPf(Double pf) {
		this.pf = pf;
	}

	public Double getPt() {
		return pt;
	}

	public void setPt(Double pt) {
		this.pt = pt;
	}

	public Double getEsi() {
		return esi;
	}

	public void setEsi(Double esi) {
		this.esi = esi;
	}

	public Double getActualSalary() {
		return actualSalary;
	}

	public void setActualSalary(Double actualSalary) {
		this.actualSalary = actualSalary;
	}

	public Double getDeductedSalary() {
		return deductedSalary;
	}

	public void setDeductedSalary(Double deductedSalary) {
		this.deductedSalary = deductedSalary;
	}

	public Double getPayable() {
		return payable;
	}

	public void setPayable(Double payable) {
		this.payable = payable;
	}

	public Double getOtAmount() {
		return otAmount;
	}

	public void setOtAmount(Double otAmount) {
		this.otAmount = otAmount;
	}

	public Double getOtAllowance() {
		return otAllowance;
	}

	public void setOtAllowance(Double otAllowance) {
		this.otAllowance = otAllowance;
	}

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance(Double specialAllowance) {
		this.specialAllowance = specialAllowance;
	}

	public int getNonPaidLeavesDays() {
		return nonPaidLeavesDays;
	}

	public void setNonPaidLeavesDays(int nonPaidLeavesDays) {
		this.nonPaidLeavesDays = nonPaidLeavesDays;
	}

	public Double getNonPaidLeavesAmount() {
		return nonPaidLeavesAmount;
	}

	public void setNonPaidLeavesAmount(Double nonPaidLeavesAmount) {
		this.nonPaidLeavesAmount = nonPaidLeavesAmount;
	}

	public Double getConveyance() {
		return conveyance;
	}

	public void setConveyance(Double conveyance) {
		this.conveyance = conveyance;
	}

	public Double getIt() {
		return it;
	}

	public void setIt(Double it) {
		this.it = it;
	}

	public String getEmpRole() {
		return empRole;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

	@Override
	public String toString() {
		return "GenerateSalaryStatement [smartId=" + smartId + ", month=" + month + ", year=" + year + ", entryTime="
				+ entryTime + ", empName=" + empName + ", empDesignation=" + empDesignation + ", empRole=" + empRole
				+ ", updSmartId=" + updSmartId + ", basicSalary=" + basicSalary + ", hra=" + hra + ", pf=" + pf
				+ ", pt=" + pt + ", esi=" + esi + ", conveyance=" + conveyance + ", it=" + it + ", actualSalary="
				+ actualSalary + ", deductedSalary=" + deductedSalary + ", payable=" + payable + ", otAmount="
				+ otAmount + ", otAllowance=" + otAllowance + ", specialAllowance=" + specialAllowance
				+ ", nonPaidLeavesDays=" + nonPaidLeavesDays + ", nonPaidLeavesAmount=" + nonPaidLeavesAmount + "]";
	}
	
	
	
}
