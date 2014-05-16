package com.envived.android.features.order;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.envived.android.EnvivedFeatureDataRetrievalService;
import com.envived.android.api.agent.FactParam;
import com.envived.android.features.Feature;
import com.envived.android.utils.EnvivedAppUpdate;
import com.envived.android.utils.EnvivedNotificationHandler;

public class OrderFeatureNotificationHandler extends EnvivedNotificationHandler {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "OrderFeatureNotificationHandler";
	
	
	@Override
	public boolean handleNotification(Context context, Intent intent,
			EnvivedAppUpdate appUpdate) {
		
		String feature = appUpdate.getFeature();
		
		// big if-else statement to determine appropriate handler class
		if (feature.compareTo(Feature.ORDER) == 0) {
			ArrayList<FactParam> params = appUpdate.getParams();
			
			// order notifications MUST have a type parameter 
			if (appUpdate.getParam("type") != null &&
					appUpdate.getParam("type").equals(OrderFeature.NEW_REQUEST_NOTIFICATION)) {
				new NewOrderRequestNotification(context, intent, appUpdate).sendNotification();
				
				return true;
			} else if (appUpdate.getParam("type") != null &&
					appUpdate.getParam("type").equals(OrderFeature.RESOLVED_REQUEST_NOTIFICATION)) {
				new ResolvedOrderRequestNotification(context, intent, appUpdate).sendNotification();
				return true;
			} else if (appUpdate.getParam("type") != null &&
					appUpdate.getParam("type").equals(OrderFeature.RETRIEVE_CONTENT_NOTIFICATION)) {
				// start the update service directly
				Intent updateService = new Intent(context, EnvivedFeatureDataRetrievalService.class);
				updateService.putExtra(
						EnvivedFeatureDataRetrievalService.DATA_RETRIEVE_SERVICE_INPUT, appUpdate);
				
				context.startService(updateService);
			}
			
			Log.d(TAG, "Order notification dispatch error: 'type` parameter missing or unknown in " 
						+ params);
		}
		
		return false;
		
	}

}
