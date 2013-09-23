package com.envived.android.api.user;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;
import com.envived.android.HomeActivity;
import com.envived.android.R;
import com.envived.android.api.Location;
import com.envived.android.features.Feature;
import com.envived.android.features.program.ProgramBySessionFragment;

public class UserProfileActivity extends SherlockFragmentActivity {

    private static final String TAG = "UserProfileActivity";

    private static final String[] TAGS = { "research", "exhibition", "leisure" };
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

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
                android.R.layout.simple_list_item_1, android.R.id.text1, TAGS);
        // Set up the dropdown list navigation in the action bar.
        mActionBar.setListNavigationCallbacks(adapter, new ListListener(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState
                            .getInt(STATE_SELECTED_NAVIGATION_ITEM));
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
            mFragment = (SherlockFragment) SherlockFragment.instantiate(
                    mActivity, DummySectionFragment.class.getName());
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
                    itemPosition + 1);
            mFragment.setArguments(args);
            FragmentManager fragmentManager = ((SherlockFragmentActivity) mActivity)
                    .getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.profile_container, mFragment).commit();
            return true;
        }

    }

    /**
     * A dummy fragment
     */
    public static class DummySectionFragment extends SherlockFragment {

        public static final String ARG_SECTION_NUMBER = "placeholder_text";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER)));
            return textView;
        }
    }

}
