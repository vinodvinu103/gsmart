package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="pay_roll")
public class PayRoll {
	
	@Id
	@Column(name="smart_Id")
	private String smartId;
	
	@Column(name="role")
	private String role;
	
	@Column(name="first_Name")
	private String firstName;
	
	@Column(name="pf") 
	private int pf;
	
	@Column(name="esi")
	private int esi;
	
	@Column(name="total_Salary")
	private double totalSalary;
	
	@Column(name="deduction")
	private double deduction;
	
	@Lob @Column(name = "salry_Slip")
	private byte[] salarySlip;
	
	
	
	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getPf() {
		return pf;
	}

	public void setPf(int pf) {
		this.pf = pf;
	}

	public int getEsi() {
		return esi;
	}

	public void setEsi(int esi) {
		this.esi = esi;
	}

	public double getTotalSalary() {
		return totalSalary;
	}

	public void setTotalSalary(double totalSalary) {
		this.totalSalary = totalSalary;
	}

	public double getDeduction() {
		return deduction;
	}

	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}

	public byte[] getSalarySlip() {
		return salarySlip;
	}

	public void setSalarySlip(byte[] salarySlip) {
		this.salarySlip = salarySlip;
	}

	
	
}
