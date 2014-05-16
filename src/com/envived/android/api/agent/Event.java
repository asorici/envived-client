package com.envived.android.api.agent;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private EventPerformative performative;
	@SerializedName("event_label") private String eventLabel;
	@SerializedName("subject_label") private String subjectLabel;
	@SerializedName("object_label") private String objectLabel;
	
	public enum EventPerformative {
		INSERT, DELETE
	}
	
	public EventPerformative getPerformative() {
		return performative;
	}
	
	public void setPerformative(EventPerformative performative) {
		this.performative = performative;
	}
	
	public String getEventLabel() {
		return eventLabel;
	}
	
	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
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
		return "Event [performative=" + performative + ", eventLabel="
				+ eventLabel + ", subjectLabel=" + subjectLabel
				+ ", objectLabel=" + objectLabel + "]";
	}
}