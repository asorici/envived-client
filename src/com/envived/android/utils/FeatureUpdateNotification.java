package com.envived.android.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.envived.android.GCMIntentService;
import com.envived.android.R;
import com.envived.android.features.Feature;
import com.envived.android.features.order.OrderFeature;

public abstract class FeatureUpdateNotification extends EnvivedNotification {
	
	private String mFeature;
	private String mUpdateType;
	
	public FeatureUpdateNotification(Context context, Intent intent,
			EnvivedNotificationContents notificationContents) {
		super(context, intent, notificationContents);
		
		mFeature = notificationContents.getFeature();
		mUpdateType = notificationContents.getParams().optString("type");
	}
	
	@Override
	public void sendNotification() {
		// Create launcher intent
		Intent launcher = new Intent(Intent.ACTION_SYNC);
		launcher.setComponent(new ComponentName(mContext,
				com.envived.android.EnvivedAppActivity.class));
		launcher.addCategory(Intent.CATEGORY_LAUNCHER);
		launcher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add extras
		launcher.putExtra(GCMIntentService.NOTIFICATION, true);
		launcher.putExtra(EnvivedNotificationContents.LOCATION_URI, mNotificationContents.getLocationUrl());
		launcher.putExtra(EnvivedNotificationContents.FEATURE, mNotificationContents.getFeature());
		launcher.putExtra(EnvivedNotificationContents.RESOURCE_URI, mNotificationContents.getResourceUrl());
		launcher.putExtra(EnvivedNotificationContents.PARAMS, mNotificationContents.getParams().toString());
	}

}
