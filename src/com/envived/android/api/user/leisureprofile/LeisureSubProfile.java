package com.envived.android.api.user.leisureprofile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.envived.android.api.user.UserProfileConfig.UserSubProfileType;
import com.envived.android.api.user.UserSubProfile;

public class LeisureSubProfile extends UserSubProfile {

	private String[] leisureInterests;
	
	public LeisureSubProfile(UserSubProfileType type) {
		super(type);
		leisureInterests = new String[] {"n.a."};
	}
	
	public LeisureSubProfile(UserSubProfileType type, String[] leisureInterests) {
		super(type);
		this.leisureInterests = leisureInterests;
	}

	@Override
	protected UserSubProfile parseProfileData(JSONObject user) {
		String[] leisureInterests = {"n.a."};
		
		//System.err.println("[DEBUG]>> user profile JSONObject: " + user.toString());
		
		JSONObject leisure_profile = (JSONObject)user.opt(UserSubProfileType.leisure.name());
		
		if (leisure_profile != null) {
			
			JSONArray leisure_interests = leisure_profile.optJSONArray("leisure_interests");
			if (leisure_interests != null) {
				int len = leisure_interests.length();
				leisureInterests = new String[len];
				 
				for (int i = 0; i < len; i++) {
					leisureInterests[i] = leisure_interests.optString(i, "n.a.");
				}
			}
			
			return new LeisureSubProfile(UserSubProfileType.leisure, leisureInterests);
		}
		
		return null;
	}

	@Override
	protected JSONObject toJSON() throws JSONException {
		JSONObject leisure_profile = new JSONObject();
		
		// build leisure_interests list
		JSONArray leisure_interests = new JSONArray();
		for (int k = 0; k < leisureInterests.length; k++) {
			leisure_interests.put(leisureInterests[k]);
		}
		
		//System.err.println("[DEBUG]>> checked in people research_interests: " + research_interests);
		
		// build leisure_profile hash
		leisure_profile.put("leisure_interests", leisure_interests);
		
		return leisure_profile;
	}

	public String[] getLeisureInterests() {
		return leisureInterests;
	}

}
