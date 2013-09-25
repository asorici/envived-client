package com.envived.android.api.user;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class UserProfileConfig {
    public static enum UserSubProfileType {
        research, exhibition, leisure, base
    }

    public static String profileSuffix = "profile";

    public static Map<UserSubProfileType, String> subProfileClassMap;
    static {
        subProfileClassMap = new HashMap<UserSubProfileType, String>();
        subProfileClassMap.put(UserSubProfileType.research,
                "com.envived.android.api.user.ResearchSubProfile");
        subProfileClassMap.put(UserSubProfileType.exhibition,
                "com.envived.android.api.user.ExhibitionSubProfile");
        subProfileClassMap.put(UserSubProfileType.leisure,
                "com.envived.android.api.user.LeisureSubProfile");
    }

    public static String[] getSubProfiles() {
        String[] names = new String[UserSubProfileType.values().length - 1];
        for (int i = 0; i < names.length; i++)
            names[i] = UserSubProfileType.values()[i].name();
        return names;
    }

}
