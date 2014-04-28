package com.envived.android.api.agent;

import java.io.Serializable;

public class FactParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String value;
	
	public FactParam(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FactParam [name=" + name + ", value=" + value + "]";
	}
}
