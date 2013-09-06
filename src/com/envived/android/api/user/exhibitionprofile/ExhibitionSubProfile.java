package com.envived.android.api.user.exhibitionprofile;

import org.json.JSONException;
import org.json.JSONObject;

import com.envived.android.api.user.UserProfileConfig.UserSubProfileType;
import com.envived.android.api.user.UserSubProfile;

public class ExhibitionSubProfile extends UserSubProfile {

	public ExhibitionSubProfile(UserSubProfileType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected UserSubProfile parseProfileData(JSONObject user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject toJSON() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
