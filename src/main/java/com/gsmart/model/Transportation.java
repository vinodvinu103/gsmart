package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSPOTATION_FEE")
public class Transportation {

	
	@Id
	@Column(name = "ENTRY_TIME")
	private String entrytime;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hid")
	private Hierarchy hierarchy;
	
	@Column(name = "IS_ACTIVE")
	private String isactive;
	
	@Column(name = "UPDATED_TIME")
	private String UpdatedTime;
	

	
	@Column(name = "TRANSFEE")
	private int transfee;
	

	@Column(name = "EXIT_TIME")
	private String exittime;

	@Column(name = "DISTANCE")
	private String distance;

	public String getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(String entrytime) {
		this.entrytime = entrytime;
	}

	
	

	public String getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		UpdatedTime = updatedTime;
	}

	
	
	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	

	

	public String getExittime() {
		return exittime;
	}

	public void setExittime(String exittime) {
		this.exittime = exittime;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getTransfee() {
		return transfee;
	}

	public void setTransfee(int transfee) {
		this.transfee = transfee;
	}
	



	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Override
	public String toString() {
		return "Transportation [\t transfee=" + transfee + ",\t distance=" + distance + ",\t exittime=" + exittime
				+ ",\t entrytime=" + entrytime + "]";
	}

	
}