package com.envived.android.api.agent;

import java.io.Serializable;

public class Fact implements Serializable {
	private static final long serialVersionUID = 1L;
	private String label;
	private String subject;
	private String object;
	
	public Fact(String label, String subject, String object) {
		this.label = label;
		this.subject = subject;
		this.object = object;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
	
	public String toString() {
		return label + " " + subject + " " + object;
	}
}
