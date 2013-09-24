package com.envived.android.api.user.exhibitionprofile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.envived.android.api.user.UserProfileConfig.UserSubProfileType;
import com.envived.android.api.user.UserSubProfile;

public class ExhibitionSubProfile extends UserSubProfile {

	private String organization;
	private String role;
	private String[] exhibitionInterests;
	
	public ExhibitionSubProfile(UserSubProfileType type) {
		super(type);
		organization = "n.a.";
		role = "n.a.";
		exhibitionInterests = new String[] {"n.a."};
	}
	
	public ExhibitionSubProfile(UserSubProfileType type, String organization, String role, String[] exhibitionInterests) {
		super(type);
		this.organization = organization;
		this.role = role;
		this.exhibitionInterests = exhibitionInterests;
	}

	@Override
	protected UserSubProfile parseProfileData(JSONObject user) {
		String organization = "n.a.";
		String role = "n.a.";
		String[] exhibitionInterests = {"n.a."};
		
		//System.err.println("[DEBUG]>> user profile JSONObject: " + user.toString());
		
		JSONObject exhibition_profile = (JSONObject)user.opt(UserSubProfileType.exhibitionprofile.name());
		
		if (exhibition_profile != null) {
			organization = exhibition_profile.optString("organization", "n.a.");
			role = exhibition_profile.optString("role", "n.a.");
			
			JSONArray exhibition_interests = exhibition_profile.optJSONArray("exhibition_interests");
			if (exhibition_interests != null) {
				int len = exhibition_interests.length();
				exhibitionInterests = new String[len];
				 
				for (int i = 0; i < len; i++) {
					exhibitionInterests[i] = exhibition_interests.optString(i, "n.a.");
				}
			}
			
			return new ExhibitionSubProfile(UserSubProfileType.exhibitionprofile, organization, role, exhibitionInterests);
		}
		
		return null;
	}

	@Override
	protected JSONObject toJSON() throws JSONException {
		JSONObject exhibition_profile = new JSONObject();
		
		// build exhibition_interests list
		JSONArray exhibition_interests = new JSONArray();
		for (int k = 0; k < exhibitionInterests.length; k++) {
			exhibition_interests.put(exhibitionInterests[k]);
		}
		
		//System.err.println("[DEBUG]>> checked in people research_interests: " + research_interests);
		
		// build exhibition_profile hash
		exhibition_profile.put("organization", organization);
		exhibition_profile.put("role", role);
		exhibition_profile.put("exhibition_interests", exhibition_interests);
		
		return exhibition_profile;
	}

	public String getOrganization() {
		return organization;
	}

	public String getRole() {
		return role;
	}

	public String[] getExhibitionInterests() {
		return exhibitionInterests;
	}

}
