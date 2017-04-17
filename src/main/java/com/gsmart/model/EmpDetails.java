package com.gsmart.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

@SuppressWarnings({ "serial", "deprecation" })
@Entity
@Table(name = "EMP_DETAILS")
@IdClass(com.gsmart.model.EmpDetailsCompoundKey.class)
public class EmpDetails implements Serializable {

	@Id
	@Column(name = "SMART_ID")
	@Index(name = "smartId")
	private Integer smartId;
	
	@Id
	@Column(name = "ATTRIBUTE")
	private String attribute;
	
	@Column(name = "VALUES1")
	private String values;

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public Integer getSmartId() {
		return smartId;
	}

	public void setSmartId(Integer smartId) {
		this.smartId = smartId;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}