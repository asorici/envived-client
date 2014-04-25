package com.envived.android.utils;

import android.content.Context;
import android.content.Intent;

public class EnvivedEventDispatcher extends EnvivedReceiver {
	
	
	/*
	 * TODO: create a new class to hold the EnvivedEventContents
	 * 	- typically, this should be a list of dictionaries with the following form:
	 * 		{"performative" : <event_performative>,
	 * 		 ""
	 * 		}
	 */
	@Override
	public boolean handleNotification(Context context, Intent intent,
			EnvivedUpdateContents notificationContents) {
		// TODO Auto-generated method stub
		return false;
	}

}
