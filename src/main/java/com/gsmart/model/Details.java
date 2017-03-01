package com.gsmart.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "DETAILS_MASTER")
@IdClass(com.gsmart.model.DetailsCompoundKey.class)
public class Details implements Serializable {

	@Id
	@Column(name = "SMART_ID")
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