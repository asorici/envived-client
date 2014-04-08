package com.envived.android.utils;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EnvivedUpdateContents implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String TAG = "EnvivedNotificationContents";
	public static final String FEATURE = "feature";
	public static final String LOCATION_URI = "location_uri";
	public static final String RESOURCE_URI = "resource_uri";
	public static final String PARAMS = "params";
	
	public static final String INTENT_EXTRA_PARAMS = "com.envived.android.notification_params";
	
	private String mLocationUrl;
	private String mFeature;
	private String mResourceUrl;
	private String mJSONSerializedParams;
	
	public EnvivedUpdateContents(String locationUrl, String feature, String resourceUrl, String params) {
		mLocationUrl = locationUrl;
		mFeature = feature;
		mResourceUrl = resourceUrl;
		mJSONSerializedParams = params;
	}
	
	public static EnvivedUpdateContents extractFromIntent(Context context, Intent intent) {
		String locationUri = intent.getStringExtra(LOCATION_URI);
		if (locationUri == null) {
			Log.d(TAG, "Location URI missing from GCM Envived Message. Notification aborted.");
			return null;
		}
		
		String feature = intent.getStringExtra(FEATURE);
		if (feature == null) {
			Log.d(TAG, "Feature type missing from GCM Envived Message. Notification aborted");
			return null;
		}
		
		String resourceUri = intent.getStringExtra(RESOURCE_URI);
		if (resourceUri == null) {
			Log.d(TAG, "Feature resourceUri missing from GCM Envived Message. Notification aborted");
			return null;
		}
		
		String params = intent.getStringExtra(PARAMS);
		if (params == null) {
			Log.d(TAG, "Notification parameters missing from GCM Envived Message. Notification aborted");
			return null;
		}
		
		// just see if JSON serialized parameters are parsable
		try {
			new JSONObject(params);
		} catch(JSONException ex) {
			Log.d(TAG, "Notification parameters (" + params + ") from GCM Envived Message could not be parsed. " +
					"Notification aborted");
			return null;
		}
		
		return new EnvivedUpdateContents(locationUri, feature, resourceUri, params);
	}

	
	public String getLocationUrl() {
		return mLocationUrl;
	}

	public String getFeature() {
		return mFeature;
	}

	public String getResourceUrl() {
		return mResourceUrl;
	}

	public JSONObject getParams() {
		try {
			return new JSONObject(mJSONSerializedParams);
		} catch (JSONException e) {
			Log.d(TAG, "ERROR parsing Envived serialized notification parameters to JSON", e);
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "[" + "feature: " + mFeature + ", " 
					+ "resourceURI: " + mResourceUrl + ", "
					+ "locationURI: " + mLocationUrl + ", "
					+ "params: " + mJSONSerializedParams + "]";
	}
}