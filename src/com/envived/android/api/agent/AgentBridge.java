package com.envived.android.api.agent;

import java.util.ArrayList;

import net.xqhs.graphs.graph.Edge;
import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleGraph;
import net.xqhs.graphs.graph.SimpleNode;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.envived.android.utils.EnvivedEvent;


public class AgentBridge extends IntentService {
	private static final String TAG = "AgentBridge";
	private ArrayList<SimpleGraph> fact_graphs;
	private ArrayList<SimpleGraph> insert_event_graphs;
	private ArrayList<SimpleGraph> update_event_graphs;
	
	public AgentBridge() {
		super("Agent Bridge");
		fact_graphs = new ArrayList<SimpleGraph>();
		insert_event_graphs = new ArrayList<SimpleGraph>();
		update_event_graphs = new ArrayList<SimpleGraph>();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getSerializableExtra("envived_event") != null) {
			EnvivedEvent e = (EnvivedEvent) intent.getSerializableExtra("envived_event");
			for (Fact f : e.getFacts()) {
				SimpleGraph g = new SimpleGraph();
				Node n1 = new SimpleNode(f.getObjectLabel());
				Node n2 = new SimpleNode(f.getSubjectLabel());
				Edge edge = new SimpleEdge(n1, n2, f.getFactLabel());
				g.addNode(n1);
				g.addNode(n2);
				g.addEdge(edge);
				fact_graphs.add(g); 
			}
			for (Event event : e.getEvents()) {
				SimpleGraph g = new SimpleGraph();
				Node n1 = new SimpleNode(event.getObjectLabel());
				Node n2 = new SimpleNode(event.getSubjectLabel());
				Edge edge = new SimpleEdge(n1, n2, event.getEventLabel());
				g.addNode(n1);
				g.addNode(n2);
				g.addEdge(edge);
				if (event.getPerformative() == Event.EventPerformative.INSERT) {
					insert_event_graphs.add(g);
				} else if (event.getPerformative() == Event.EventPerformative.DELETE) {
					update_event_graphs.add(g);
				}
			}
		}
	}
}
