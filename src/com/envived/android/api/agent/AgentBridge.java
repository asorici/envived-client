package com.envived.android.api.agent;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.envived.android.utils.EnvivedEvent;

/*
 * Creez un receiver pe c
 */

public class AgentBridge extends IntentService {
	private static final String TAG = "AgentBridge";
	
	public AgentBridge() {
		super("Agent Bridge");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getSerializableExtra("envived_event") != null) {
			EnvivedEvent e = (EnvivedEvent) intent.getSerializableExtra("envived_event");
			Log.d(TAG, e.toString());
		}
	}

}
