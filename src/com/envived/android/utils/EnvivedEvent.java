package com.envived.android.utils;

import java.io.Serializable;
import java.util.ArrayList;

import com.envived.android.api.agent.Event;
import com.envived.android.api.agent.Fact;

public class EnvivedEvent implements Serializable{
	private static final long serialVersionUID = 1L;
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
