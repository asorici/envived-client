package com.envived.android.features.conferencerole;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.envived.android.Envived;
import com.envived.android.api.EnvSocialResource;
import com.envived.android.api.exceptions.EnvSocialContentException;
import com.envived.android.features.Feature;
import com.envived.android.utils.FeatureDbHelper;
import com.envived.android.utils.Preferences;

public class ConferenceRoleFeature extends Feature {
	private static final long serialVersionUID = 1L;
	
	private static final String ROLE_TEXT = "role";
	
	private String mRole = null; 

	public ConferenceRoleFeature(String category, int version,
			Calendar timestamp, boolean isGeneral, String resourceUrl,
			String environmentUrl, String areaUrl, String data,
			boolean virtualAccess) {
		super(category, version, timestamp, isGeneral, resourceUrl, environmentUrl,
				areaUrl, data, virtualAccess);
	}
	
	public String getRole() {
		return mRole;
	}

	@Override
	protected void featureInit(boolean insert) throws EnvSocialContentException {
		String serializedData = retrievedData;
		try {			
			JSONObject roleData = new JSONObject(serializedData);
			mRole = roleData.optString(ROLE_TEXT, null);
		} 
		catch (JSONException e) {
			throw new EnvSocialContentException(serializedData, EnvSocialResource.FEATURE, e);
		}
	}

	@Override
	protected void featureUpdate() throws EnvSocialContentException {
		try {
			if (retrievedData != null) {
				JSONObject roleData = new JSONObject(retrievedData);
				mRole = roleData.optString(ROLE_TEXT, null);
			}
		} catch (JSONException e) {
			throw new EnvSocialContentException(retrievedData, EnvSocialResource.FEATURE, e);
		}
	}

	@Override
	protected void featureCleanup(Context context) {		
	}

	@Override
	protected void featureClose(Context context) {		
	}

	@Override
	public boolean hasLocalQuerySupport() {
		return false;
	}

	@Override
	public int getDisplayThumbnail() {
		return 0;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public FeatureDbHelper getLocalDatabaseSupport() {
		return null;
	}

	@Override
	public boolean hasLocalDatabaseSupport() {
		return false;
	}

}
