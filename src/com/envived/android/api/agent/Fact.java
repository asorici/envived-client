package com.envived.android.api.agent;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Fact implements Serializable {
	private static final long serialVersionUID = 1L;
	@SerializedName("fact_label") private String factLabel;
	@SerializedName("subject_label") private String subjectLabel;
	@SerializedName("object_label") private String objectLabel;
	
	public String getFactLabel() {
		return factLabel;
	}
	
	public void setFactLabel(String factLabel) {
		this.factLabel = factLabel;
	}
	
	public String getSubjectLabel() {
		return subjectLabel;
	}
	
	public void setSubjectLabel(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}
	
	public String getObjectLabel() {
		return objectLabel;
	}
	
	public void setObjectLabel(String objectLabel) {
		this.objectLabel = objectLabel;
	}

	@Override
	public String toString() {
		return "Fact [factLabel=" + factLabel + ", subjectLabel="
				+ subjectLabel + ", objectLabel=" + objectLabel + "]";
	}
}