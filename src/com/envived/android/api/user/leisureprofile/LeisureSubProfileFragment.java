package com.envived.android.api.user.leisureprofile;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.envived.android.R;
import com.envived.android.api.user.UserProfileSettings;
import com.envived.android.api.user.UserProfileConfig.UserSubProfileType;
import com.envived.android.api.user.researchprofile.ResearchSubProfile;
import com.envived.android.utils.Preferences;
import com.envived.android.utils.Utils;

public class LeisureSubProfileFragment extends SherlockFragment implements OnClickListener {

    public static final String ARG_SECTION_NUMBER = "1";
    private static final int RESULT_SETTINGS = 1;
    private static final String TAG = "LeisureSubProfileFragment";
    
    private MultiAutoCompleteTextView mInterests;
    private Button mBtnSubmit;
    
    private LeisureSubProfile profile;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.subprofile_leisure, container, false);
        
        mInterests = (MultiAutoCompleteTextView) view.findViewById(R.id.txt_interests_subprofile_leisure);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit_subprofile_leisure);

        /*
         * SUGGESTIONS
         */
        ArrayAdapter<String> arrayTokens = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, Utils.suggestedWords);
        mInterests.setAdapter(arrayTokens);
        mInterests.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        Log.d(TAG, "CHOOSE INTERESTS REGISTER: " + mInterests.getText().toString());
        
        // Verify existence of shared preferences
        verifySharedPrefs();
        
        mBtnSubmit.setOnClickListener(this);
        
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            saveSubProfile();
            //new ProfileSaveTask().execute();
        }
    }
    
    private void verifySharedPrefs() {
        try {
            if (Preferences.isResearchSubProfile(getActivity())) {
                mInterests.setText(getInterests());
            }
        } catch (JSONException e) {
            Toast toast = Toast.makeText(getActivity(), R.string.msg_bad_subprofile_response, Toast.LENGTH_LONG);
            toast.show();
            Log.d(TAG, "Exception in getting subprofile " + e.getMessage());
        }
    }

    private void saveSubProfile() {
        Preferences.saveLeisureSubProfile(getActivity(), mInterests.getText().toString());
        Toast toast = Toast.makeText(getActivity(), R.string.msg_subprofile_saved, Toast.LENGTH_LONG);
        toast.show();
    }
    
    private String getInterests() throws JSONException {
        JSONObject jo = Preferences.getLeisureSubProfile(getActivity());
        return jo.get("leisureInterests").toString();
    }
    
}
