package com.envived.android.utils;

import java.io.Serializable;

import org.json.JSONObject;

public class EnvivedMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	String type;
    String timestamp;
    JSONObject content;
    
    public EnvivedMessage() {
    	
    }
    
    public String toString() {
		return "type: " + type + "\ncontent: " + content + "\ntimestamp: " + timestamp;
    }
}
