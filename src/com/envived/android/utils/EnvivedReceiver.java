package com.envived.android.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public abstract class EnvivedReceiver extends BroadcastReceiver {
	private static final String TAG = "EnvivedReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		EnvivedAppUpdate appUpdate = (EnvivedAppUpdate)intent.getExtras().getSerializable("envived_app_update");
		
		if (appUpdate != null && handleNotification(context, intent, appUpdate)) {
			abortBroadcast();
		}
		else if(appUpdate == null) {
			Log.d(TAG, "Error receiving ENVIVED notification. Notification contents are missing " +
					"or unparseable in intent: " + intent.getDataString());
		}
	}
	
	public abstract boolean handleNotification(Context context, 
			Intent intent, EnvivedAppUpdate notificationContents);
}
