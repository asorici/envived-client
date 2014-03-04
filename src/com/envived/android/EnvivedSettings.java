package com.envived.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.envived.android.utils.EnvivedMessageService;

public class EnvivedSettings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String KEY_ENVIVED_NOTIFICATIONS = "envived_notifications";
	public static final String TAG = "EnvivedSettings";
    
    private CheckBoxPreference mEnvivedNotificationsPreference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the XML preferences file
        addPreferencesFromResource(R.xml.envived_preferences);

        // Get a reference to the Envived notifications preference
        mEnvivedNotificationsPreference = (CheckBoxPreference)getPreferenceScreen().findPreference(
        		KEY_ENVIVED_NOTIFICATIONS);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(KEY_ENVIVED_NOTIFICATIONS)) {
			if (!sharedPreferences.getBoolean(KEY_ENVIVED_NOTIFICATIONS, true)) {
				// the user has deactivated notifications
				Log.d(TAG, "stopping service");
				stopService(new Intent(EnvivedSettings.this, EnvivedMessageService.class));
			} else {
				// the user has activated notifications
				Log.d(TAG, "starting service");
				startService(new Intent(EnvivedSettings.this, EnvivedMessageService.class));
			}
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    // Set up a listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    // Unregister the listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
}
