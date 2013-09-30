package com.envived.android.api.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.envived.android.R;
import com.envived.android.api.user.researchprofile.ResearchSubProfileFragment;
import com.envived.android.api.user.leisureprofile.LeisureSubProfileFragment;
import com.envived.android.api.user.exhibitionprofile.ExhibitionSubProfileFragment;
import com.envived.android.utils.Preferences;

public class UserProfileActivity extends SherlockFragmentActivity {

    private static final String TAG = "UserProfileActivity";
    private static final int RESULT_SETTINGS = 1;

    private static final String[] SUBPROFILES = UserProfileConfig.getSubProfiles();
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String userPackage = "com.envived.android.api.user.";

    private static String mPrefAvailability = "0";
    private static boolean mPrefDisplayEmail = false;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.user_profile);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Profile");
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        initListNavigation();

    }

    private void initListNavigation() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mActionBar.getThemedContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                SUBPROFILES);
        // Set up the dropdown list navigation in the action bar.
        mActionBar.setListNavigationCallbacks(adapter, new ListListener(this));
    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        mPrefAvailability = sharedPrefs.getString("prefAvailability", "NULL");
        mPrefDisplayEmail = sharedPrefs.getBoolean("prefDisplayEmail", false);

        Log.d(TAG, "Preferences: " + mPrefAvailability + " - "
                + mPrefDisplayEmail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case RESULT_SETTINGS:
            showUserSettings();
            break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case R.id.user_profile_menu:
            Intent i = new Intent(this, UserProfileSettings.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Context context = getApplicationContext();

        // menu.removeItem(R.id.home_menu_settings);

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ListListener extends SherlockFragmentActivity implements
            ActionBar.OnNavigationListener {

        private final Activity mActivity;
        private SherlockFragment mFragment;

        public ListListener(Activity mActivity) {
            this.mActivity = mActivity;
        }

        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            // When the given dropdown item is selected, show its contents in
            // the
            // container view.

            // mFragment = (SherlockFragment)
            // SherlockFragment.instantiate(mActivity,
            // DummySectionFragment.class.getName());

            String classSuffix = "SubProfileFragment";
            String className = SUBPROFILES[itemPosition].substring(0, 1)
                    .toUpperCase()
                    + SUBPROFILES[itemPosition].substring(1,
                            SUBPROFILES[itemPosition].length()) + classSuffix;
            String classNameWithPackage = userPackage
                    + SUBPROFILES[itemPosition] + "profile." + className;

            mFragment = (SherlockFragment) SherlockFragment.instantiate(
                    mActivity, classNameWithPackage);
            Log.d(TAG, "!!!!!!!!!! " + classNameWithPackage);

            FragmentManager fragmentManager = ((SherlockFragmentActivity) mActivity)
                    .getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.profile_container, mFragment).commit();
            return true;
        }

    }

}
