package com.envived.android.api.agent;

import java.util.ArrayList;

import net.xqhs.graphs.graph.Edge;
import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleGraph;
import net.xqhs.graphs.graph.SimpleNode;
import android.R;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

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

	@SuppressLint("NewApi")
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
		
		if (Build.VERSION.SDK_INT >= 11) {
			Intent i = new Intent(this, AgentBridge.class);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
			
			Notification n  = new Notification.Builder(this)
	        .setContentTitle("Notification")
	        .setContentText("Test subject")
	        .setSmallIcon(R.drawable.ic_dialog_info)
	        .setContentIntent(pIntent)
	        .setAutoCancel(true).build();
			
			NotificationManager notificationManager = 
					  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	
			notificationManager.notify(0, n);
		}
	}
}
