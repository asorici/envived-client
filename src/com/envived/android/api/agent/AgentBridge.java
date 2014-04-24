package com.envived.android.api.agent;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/*
 * Creez un receiver pe c
 */

public class AgentBridge extends IntentService {
	private static final String TAG = "AgentBridge";
	
	public AgentBridge() {
		super("Agent Bridge");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.d(TAG, arg0.getExtras().toString());
		Fact f = (Fact) arg0.getSerializableExtra("fact0");
		Log.d(TAG, f.toString());
	}

}
