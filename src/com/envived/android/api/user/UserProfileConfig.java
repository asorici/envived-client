package com.envived.android.api.user;

import java.util.HashMap;
import java.util.Map;

public class UserProfileConfig {
	public static enum UserSubProfileType {
		researchprofile 
	}
	
	public static Map<UserSubProfileType, String> subProfileClassMap;
	static {
		subProfileClassMap = new HashMap<UserSubProfileType, String>();
		subProfileClassMap.put(UserSubProfileType.researchprofile, 
								"com.envived.android.api.user.ResearchSubProfile");
	}
}
