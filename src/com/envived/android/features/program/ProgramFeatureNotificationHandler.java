package com.envived.android.features.program;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.envived.android.EnvivedFeatureDataRetrievalService;
import com.envived.android.features.Feature;
import com.envived.android.utils.EnvivedAppUpdate;
import com.envived.android.utils.EnvivedNotificationHandler;

public class ProgramFeatureNotificationHandler extends EnvivedNotificationHandler {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "ProgramFeatureNotificationHandler";
	
	@Override
	public boolean handleNotification(Context context, Intent intent,
			EnvivedAppUpdate appUpdate) {
		
		//Log.d(TAG,"Received the following program update contents:" + notificationContents);
		
		String feature = appUpdate.getFeature();
		
		// big if-else statement to determine appropriate handler class
		if (feature.compareTo(Feature.PROGRAM) == 0) {
			
			if (appUpdate.getParam("type") != null 
					&& appUpdate.getParam("type").equals(Feature.RETRIEVE_CONTENT_NOTIFICATION)) {
				
				// start the update service directly
				Intent updateService = new Intent(context, EnvivedFeatureDataRetrievalService.class);
				updateService.putExtra(
						EnvivedFeatureDataRetrievalService.DATA_RETRIEVE_SERVICE_INPUT, appUpdate);
				
				context.startService(updateService);
			}
			else {
				Log.d(TAG, "Program notification dispatch error: 'type` parameter missing or unknown in " 
						+ appUpdate.getParams().toString());
			}
		}
		
		return false;
		
	}

}
