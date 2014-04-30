package com.envived.android.api.agent;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.envived.android.utils.EnvivedEvent;

import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleGraph;
import net.xqhs.graphs.graph.SimpleNode;
import net.xqhs.graphs.representation.text.TextGraphRepresentation;


public class AgentBridge extends IntentService {
	private static final String TAG = "AgentBridge";
	
	public AgentBridge() {
		super("Agent Bridge");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getSerializableExtra("envived_event") != null) {
			EnvivedEvent e = (EnvivedEvent) intent.getSerializableExtra("envived_event");
			for (Fact f : e.getFacts()) {
				SimpleGraph g = new SimpleGraph();
			}
		}
	}
}
