package com.envived.android.utils;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Acts like a factory class dispatching Envived GCM Notification messages
 * to the appropriate handler class.
 * @author alex
 *
 */
public class EnvivedAppUpdateDispatcher extends EnvivedReceiver {
	
	private static final String TAG = "EnvivedAppUpdateDispatcher";
	private static Set<EnvivedNotificationHandler> notificationHandlers;
	static {
		notificationHandlers = new HashSet<EnvivedNotificationHandler>();
	}
	
	public static void registerNotificationHandler(EnvivedNotificationHandler handler) {
		if (handler != null) {
			notificationHandlers.add(handler);
		}
	}
	
	public static void unregisterNotificationHandler(EnvivedNotificationHandler handler) {
		if (handler != null) {
			notificationHandlers.remove(handler);
		}
		Log.d("double", "dupa: " + notificationHandlers.toString());
	}
	
	@Override
	public boolean handleNotification(Context context, Intent intent,
			EnvivedAppUpdate appUpdate) {
		//Log.d(TAG,"Received the following update notification contents:" + notificationContents);
		
		// just cycle through all EnvivedNotificationHandlers until one returns true
		// discrimination is made on the value of the feature category - so no duplicates will exist
		for (EnvivedNotificationHandler handler : notificationHandlers) {
			if (handler.handleNotification(context, intent, appUpdate)) {
				// we have found our notification handler, break and return
				return true;
			}
		}
		
		return false;
	}
	
}
