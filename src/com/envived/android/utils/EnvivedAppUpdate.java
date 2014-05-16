package com.envived.android.utils;

import java.io.Serializable;
import java.util.ArrayList;

import com.envived.android.api.agent.FactParam;
import com.google.gson.annotations.SerializedName;

/***
 * EnvivedAppUpdate contains updates related to features. It's parsed from json
 * using Gson, and a typical message looks like:
 * 
 * {'location_uri': 'example_uri', 'resource_uri': 'example_uri',
 * 'feature': 'example_feature' 'params': [{'name': 'type', 'value': 'new_request'}]}
 * 
 * @author alextomescu
 *
 */
public class EnvivedAppUpdate implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FEATURE = "feature";
	public static final String LOCATION_URI = "location_uri";
	public static final String RESOURCE_URI = "resource_uri";
	public static final String PARAMS = "params";
	public static final String INTENT_EXTRA_PARAMS = "com.envived.android.notification_params";
	@SerializedName("location_uri") private String locationUri;
	@SerializedName("resource_uri") private String resourceUri;
	private String feature;
	private ArrayList<FactParam> params;

	public EnvivedAppUpdate(String locationUri, String feature,
			String resourceUri, ArrayList<FactParam> params) {
		this.locationUri = locationUri;
		this.feature = feature;
		this.resourceUri = resourceUri;
		this.params = params;
	}
	
	public String getLocationUri() {
		return locationUri;
	}

	public void setLocationUri(String locationUri) {
		this.locationUri = locationUri;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public ArrayList<FactParam> getParams() {
		return params;
	}
	
	public String getParam(String key) {
		for (FactParam param : this.params) {
			if (param.getName().equals("type")) {
				return param.getValue();
			}
		}
		return null;
	}

	public void setParams(ArrayList<FactParam> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "EnvivedAppUpdate [locationUri=" + locationUri
				+ ", resourceUri=" + resourceUri + ", feature=" + feature
				+ ", params=" + params + "]";
	}
}
