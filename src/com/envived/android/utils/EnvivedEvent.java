package com.envived.android.utils;

import java.io.Serializable;
import java.util.ArrayList;

import com.envived.android.api.agent.Event;
import com.envived.android.api.agent.Fact;

public class EnvivedEvent implements Serializable{
	private static final long serialVersionUID = 1L;
	/*private ArrayList<Fact> facts = new ArrayList<Fact>();
	private ArrayList<Event> events = new ArrayList<Event>();
	
	public EnvivedEvent(JSONObject contentJson) {
		try {
			Iterator<Object> keys = contentJson.keys();
	    	
	    	while (keys.hasNext()) {
	    		String key = (String)keys.next();
	    		
	    		
	    		if (key.equals("facts")) {
	    			JSONArray jsonArray = contentJson.getJSONArray("facts");  
	    			for (int i = 0; i < jsonArray.length(); i++) {
    					JSONObject jsonFact = new JSONObject(jsonArray.getString(i));
    					facts.add(new Fact(
    							jsonFact.getString("fact_label"),
    							jsonFact.getString("subject_label"),
    							jsonFact.getString("object_label")));
    				}
	    		} else if (key.equals("events")) {
	    			JSONArray jsonArray = contentJson.getJSONArray("events");
	    			for (int i = 0; i < jsonArray.length(); i++) {
    					JSONObject jsonEvent = new JSONObject(jsonArray.getString(i));
    					events.add(new Event(
    							jsonEvent.getString("performative"),
    							jsonEvent.getString("event_label"),
    							jsonEvent.getString("subject_label"),
    							jsonEvent.getString("object_label")));
    				}
	    		}
	    	}
	    	
	    	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Fact> getFacts() {
		return facts;
	}

	public void setFacts(ArrayList<Fact> facts) {
		this.facts = facts;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
	
	public String toString() {
		String s = "";
		for (Fact f : facts) {
			s += f.toString() + " ";
		}
		for (Event e : events) {
			s += e.toString() + " ";
		}
		return s;
	}*/
	
	private ArrayList<Fact> facts;
	private ArrayList<Event> events;
	
	public ArrayList<Fact> getFacts() {
		return facts;
	}
	
	public void setFacts(ArrayList<Fact> facts) {
		this.facts = facts;
	}
	
	public ArrayList<Event> getEvents() {
		return events;
	}
	
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return "EnvivedEvent [facts=" + facts + ", events=" + events + "]";
	}
}
