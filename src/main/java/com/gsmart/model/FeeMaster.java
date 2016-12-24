package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * class-name: FeeMaster.java Assigning fee for students who enrolled in the
 * school
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 *
 */
@Entity
@Table(name = "FEE_MASTER")
@IdClass(com.gsmart.model.CompoundFeeMaster.class)
public class FeeMaster {
	/**
	 * Fee of the each standard
	 */
	@Id
	@Column(name = "STANDARD")
	private String standard;
	/**
	 * Sports fee is a fee of each student
	 */
	
	@Column(name = "SPORTS_FEE")
	private Integer sportsFee;
	/**
	 * Tuition fee is a fee of each student
	 */
	
	@Column(name = "TUITION_FEE")
	private Integer tuitionFee;
	/**
	 * Transportation fee is a fee of each student
	 */
	
	@Column(name = "TRANSPORTATION_FEE")
	private Integer transportationFee;
	/**
	 * Miscellaneous fee is a fee of each student
	 */
	
	@Column(name = "MISCELLANEOUS_FEE")
	private Integer miscellaneousFee;
	/**
	 * Total fee of each student
	 */
	@Column(name = "TOTAL_FEE")
	private Integer totalFee;
	
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;
	
	@Column(name = "EXIT_TIME")
	private String exitTime;
	
	@Column(name = "UPDATED_TIME")
	private String updatedTime;
	
	@Column(name = "ISACTIVE")
	private String isActive;
	
	
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
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

	public Integer getTransportationFee() {
		return transportationFee;
	}

	public void setTransportationFee(Integer transportationFee) {
		this.transportationFee = transportationFee;
	}

	public Integer getMiscellaneousFee() {
		return miscellaneousFee;
	}

	public void setMiscellaneousFee(Integer miscellaneousFee) {
		this.miscellaneousFee = miscellaneousFee;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	@Override
	public String toString() {
		return "FeeMaster [\n\tstandard=" + standard + ",\n\t sportsFee=" + sportsFee + ", \n\t tuitionFee=" + tuitionFee
				+ ", \n\t transportationFee=" + transportationFee + ", \n\t miscellaneousFee=" + miscellaneousFee + ", \n\t totalFee="
				+ totalFee + ", entryTime=" + entryTime + ", \n\t exitTime=" + exitTime + ", \n\t updatedTime=" + updatedTime
				+ ", isActive=" + isActive + "]";
	}
	

}