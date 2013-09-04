package com.envived.android.features.people;

import java.util.Calendar;

import android.content.Context;

import com.envived.android.R;
import com.envived.android.api.exceptions.EnvivedContentException;
import com.envived.android.features.Feature;
import com.envived.android.utils.FeatureDbHelper;

public class PeopleFeature extends Feature {
	private static final long serialVersionUID = 1L;

	public PeopleFeature(String category, int version, Calendar timestamp, boolean isGeneral, 
			String resourceUri, String environmentUri, String areaUri, String data, boolean virtualAccess) {
		
		super(category, version, timestamp, isGeneral, resourceUri, environmentUri, areaUri, data, virtualAccess);
	}
	
	
	@Override
	public boolean hasLocalDatabaseSupport() {
		return false;
	}

	@Override
	public boolean hasLocalQuerySupport() {
		return false;
	}

	@Override
	public FeatureDbHelper getLocalDatabaseSupport() {
		return null;
	}


	@Override
	protected void featureInit(boolean insert) throws EnvivedContentException {}


	@Override
	protected void featureUpdate() throws EnvivedContentException {}


	@Override
	protected void featureCleanup(Context context) {}


	@Override
	protected void featureClose(Context context) {}


	@Override
	public int getDisplayThumbnail() {
		return R.drawable.ic_envived_white; // no people thumbnail yet
	}


	@Override
	public String getDisplayName() {
		return "People";
	}

}
