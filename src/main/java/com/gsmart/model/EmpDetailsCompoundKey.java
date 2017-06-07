package com.gsmart.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class EmpDetailsCompoundKey implements Serializable {

	private Integer smartId;
	private String attribute;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((smartId == null) ? 0 : smartId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpDetailsCompoundKey other = (EmpDetailsCompoundKey) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (smartId == null) {
			if (other.smartId != null)
				return false;
		} else if (!smartId.equals(other.smartId))
			return false;
		return true;
	}

	
	
}