package com.envived.android.utils;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class EnvivedAppUpdate implements Serializable {
	private static final long serialVersionUID = 1L;
	@SerializedName("location_uri") private String locationUri;
	@SerializedName("resource_uri") private String resourceUri;
	private String feature;
	private ArrayList<FactParam> params;
	
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

	public void setParams(ArrayList<FactParam> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "EnvivedAppUpdate [locationUri=" + locationUri
				+ ", resourceUri=" + resourceUri + ", feature=" + feature
				+ ", params=" + params + "]";
	}
	
	public class FactParam implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String value;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "FactParam [name=" + name + ", value=" + value + "]";
		}
	}
}
