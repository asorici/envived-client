package com.envived.android.api.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.envived.android.api.user.UserProfileConfig.UserSubProfileType;
import com.envived.android.api.user.exhibitionprofile.ExhibitionSubProfile;
import com.envived.android.api.user.leisureprofile.LeisureSubProfile;
import com.envived.android.api.user.researchprofile.ResearchSubProfile;

public class UserProfileData {
	private String firstName;
	private String lastName;
	private String email;
	private UserSubProfile subProfile;
	//private Map<UserSubProfileType, UserSubProfile> subProfileMap;
	
	UserProfileData(String firstName, String lastName, String email, UserSubProfileType type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		if (type == UserSubProfileType.base)
			this.subProfile = null;
		else
			initSubProfile(type);
		//initSubProfileMap();
	}
	
	private void initSubProfile(UserSubProfileType type) {
		if (type.equals(UserSubProfileType.research))
			subProfile = new ResearchSubProfile(type);
		if (type.equals(UserSubProfileType.leisure))
			subProfile = new LeisureSubProfile(type);
		if (type.equals(UserSubProfileType.exhibition))
			subProfile = new ExhibitionSubProfile(type);
	}
	
	/*private void initSubProfileMap() {
		subProfileMap = new HashMap<UserProfileConfig.UserSubProfileType, UserSubProfile>();
		
		for (UserSubProfileType type : UserProfileConfig.subProfileClassMap.keySet()) {
			try {
				Class<?> subProfileClass = Class.forName(UserProfileConfig.subProfileClassMap.get(type));
				UserSubProfile subProfile = (UserSubProfile)subProfileClass.
						getConstructor(UserSubProfileType.class).newInstance(type);
				
				subProfileMap.put(type, subProfile);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	public UserSubProfile getSubProfile(UserSubProfileType type) {
		if (subProfile.profileType == type)
			return subProfile;
		return null;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public static UserProfileData parseProfileData(JSONObject user) throws JSONException {
		String firstName = user.optString("first_name", "Anonymous");
		String lastName = user.optString("last_name", "Guest");
		String email = user.optString("email", "n.a.");
		String type = user.optString("type", "base");
		UserSubProfileType subType;
		if (firstName.isEmpty())
			firstName = "Anonymous";
		if (lastName.isEmpty())  
			lastName = "Guest";
		if (email.isEmpty())  
			email = "n.a.";
		if (type.equalsIgnoreCase("leisure"))
			subType = UserSubProfileType.leisure;
		else if (type.equalsIgnoreCase("research"))
			subType = UserSubProfileType.research;
		else if (type.equalsIgnoreCase("exhibition"))
			subType = UserSubProfileType.exhibition;
		else
			subType = UserSubProfileType.base;
		 
		UserProfileData profileData = new UserProfileData(firstName, lastName, email, subType);
		
		/*JSONObject subProfiles = user.optJSONObject("subprofiles");
		if (subProfiles != null) {
			profileData.parseSubProfiles(profileData, subProfiles);
		}*/
		
		return profileData;
	}
	
	/*void parseSubProfiles(UserProfileData profileData, JSONObject subProfiles) {
		for (UserSubProfileType type : subProfileMap.keySet()) {
			UserSubProfile subProfile = subProfileMap.get(type);
			
			UserSubProfile parsedProfile = subProfile.parseProfileData(subProfiles);
			if (parsedProfile != null) {
				subProfileMap.put(type, parsedProfile);
			}
		}
	}*/


	/*public List<Map<String, JSONObject>> subProfilesToJson() {
		List<Map<String, JSONObject>> subProfileJsonList = new ArrayList<Map<String,JSONObject>>();
		for (UserSubProfileType type : subProfileMap.keySet()) {
			UserSubProfile subProfile = subProfileMap.get(type);
			
			try {
				JSONObject subProfileJson = subProfile.toJSON();
				Map<String, JSONObject> m = new HashMap<String, JSONObject>();
				m.put(type.name(), subProfileJson);
				
				subProfileJsonList.add(m);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return subProfileJsonList;
	}*/
	
	
	/*public Map<UserSubProfileType, UserSubProfile> getSubProfileMap() {
		return subProfileMap;
	}*/
	
	/*public UserSubProfile getSubProfile(UserSubProfileType type) {
		return subProfileMap.get(type);
	}*/
}
