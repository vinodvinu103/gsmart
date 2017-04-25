package com.gsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;


@SuppressWarnings("deprecation")
@Entity
@Table(name="PAY_SLIP")
public class PaySlip {
	
	@Id
	@Column(name="SMART_ID")
//	@Index(name = "smartId")
	private String smartId;
	
	@Column(name="FROM_MONTH")
	private String fromMonth;
	
	@Column(name="FROM_YEAR")
	private String fromYear;
	
	@Column(name="TO_MONTH")
	private String toMonth;
	
	@Column(name="TO_YEAR")
//	@Index(name = "toYear")
	private String toYear;
	
	
	public String getSmartId() {
		return smartId;
	}
	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}
	public String getFromMonth() {
		return fromMonth;
	}
	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}
	public String getFromYear() {
		return fromYear;
	}
	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}
	public String getToMonth() {
		return toMonth;
	}
	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}
	public String getToYear() {
		return toYear;
	}
	public void setToYear(String toYear) {
		this.toYear = toYear;
	}
	@Override
	public String toString() {
		return "PaySlip [smartId=" + smartId + ", fromMonth=" + fromMonth + ", fromYear=" + fromYear + ", toMonth="
				+ toMonth + ", toYear=" + toYear + "]";
	}
	
	
	
	
	
}
