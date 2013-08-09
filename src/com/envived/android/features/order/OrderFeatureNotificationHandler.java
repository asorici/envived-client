package com.envived.android.features.order;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.envived.android.EnvivedFeatureDataRetrievalService;
import com.envived.android.features.Feature;
import com.envived.android.utils.EnvivedNotificationContents;
import com.envived.android.utils.EnvivedNotificationHandler;

public class OrderFeatureNotificationHandler extends EnvivedNotificationHandler {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "OrderFeatureNotificationHandler";
	
	
	@Override
	public boolean handleNotification(Context context, Intent intent,
			EnvivedNotificationContents notificationContents) {
		
		String feature = notificationContents.getFeature();
		
		// big if-else statement to determine appropriate handler class
		if (feature.compareTo(Feature.ORDER) == 0) {
			JSONObject paramsJSON = notificationContents.getParams();
			
			// order notifications MUST have a type parameter 
			if (paramsJSON.optString("type", null) != null 
				&& paramsJSON.optString("type").compareTo(OrderFeature.NEW_REQUEST_NOTIFICATION) == 0) {
				new NewOrderRequestNotification(context, intent, notificationContents).sendNotification();
				
				return true;
			}
			else if (paramsJSON.optString("type", null) != null 
				&& paramsJSON.optString("type").compareTo(OrderFeature.RESOLVED_REQUEST_NOTIFICATION) == 0) {
				new ResolvedOrderRequestNotification(context, intent, notificationContents).sendNotification();
				
				return true;
			}
			else if (paramsJSON.optString("type", null) != null 
					&& paramsJSON.optString("type").compareTo(OrderFeature.RETRIEVE_CONTENT_NOTIFICATION) == 0) {
				// start the update service directly
				Intent updateService = new Intent(context, EnvivedFeatureDataRetrievalService.class);
				updateService.putExtra(
						EnvivedFeatureDataRetrievalService.DATA_RETRIEVE_SERVICE_INPUT, notificationContents);
				
				context.startService(updateService);
			}
			
			Log.d(TAG, "Order notification dispatch error: 'type` parameter missing or unknown in " 
						+ paramsJSON.toString());
		}
		
		return false;
		
	}

}
