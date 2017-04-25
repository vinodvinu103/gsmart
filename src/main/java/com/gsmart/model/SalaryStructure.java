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
@Table(name="SALARY_STRUCTURE")
@IdClass(CompoundSalaryStructure.class)
public class SalaryStructure {

	
	@Id
	@Column(name="SMART_ID")
//	@Index(name = "smartId")
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
	private String empName;
	
	@Column(name="EMP_DESIGNATION")
	private String empDesignation;
	
	@Column(name="EMP_ROLE")
//	@Index(name = "empRole")
	private String empRole;
	
	@Column(name="IS_ACTIVE")
//	@Index(name = "isActive")
	private String isActive;
	
	@Column(name="EXIT_TIME")
	private String exitTime;
	
	@Column(name="UPDATED_TIME")
	private String updatedTime;
	
	@Column(name="UPD_SMART_ID")
	private String updSmartId;
	
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
	
	@Column(name="SPECIAL_ALLOWANCE")
	private Double specialAllowance;
	
	@Column(name="OT_AMOUNT")
	private Double otAmount;
	
	@Column(name="SALARY")
	private Double salary;
	
	@Column(name="STATUTORY")
	private Double statutory;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdSmartId() {
		return updSmartId;
	}

	public void setUpdSmartId(String updSmartId) {
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

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance(Double specialAllowance) {
		this.specialAllowance = specialAllowance;
	}

	public Double getOtAmount() {
		return otAmount;
	}

	public void setOtAmount(Double otAmount) {
		this.otAmount = otAmount;
	}

	public Double getSalary() {
		return basicSalary+hra+pf+pt+esi+specialAllowance+conveyance+it;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Double getStatutory() {
		return pf+pt+esi+it;
	}

	public void setStatutory(Double statutory) {
		this.statutory = statutory;
	}

	public String getEmpRole() {
		return empRole;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

	@Override
	public String toString() {
		return "SalaryStructure [smartId=" + smartId + ", month=" + month + ", year=" + year + ", entryTime="
				+ entryTime + ", empName=" + empName + ", empDesignation=" + empDesignation + ", empRole=" + empRole
				+ ", isActive=" + isActive + ", exitTime=" + exitTime + ", updatedTime=" + updatedTime + ", updSmartId="
				+ updSmartId + ", basicSalary=" + basicSalary + ", hra=" + hra + ", pf=" + pf + ", pt=" + pt + ", esi="
				+ esi + ", conveyance=" + conveyance + ", it=" + it + ", specialAllowance=" + specialAllowance
				+ ", otAmount=" + otAmount + ", salary=" + salary + ", statutory=" + statutory + "]";
	}

	
	
	
}
