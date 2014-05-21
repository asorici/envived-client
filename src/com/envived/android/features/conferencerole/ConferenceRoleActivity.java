package com.envived.android.features.conferencerole;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.envived.android.R;
import com.envived.android.api.EnvSocialResource;
import com.envived.android.api.Location;
import com.envived.android.api.exceptions.EnvSocialContentException;
import com.envived.android.features.EnvivedFeatureActivity;
import com.envived.android.features.Feature;

public class ConferenceRoleActivity extends EnvivedFeatureActivity implements OnItemSelectedListener, OnClickListener {
	
	private LinearLayout mMainView;
	private Spinner mRoleSpinner;
	private EditText mChairPassword;
	private Button mButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        setContentView(R.layout.conference_role);
        
        mMainView = (LinearLayout) findViewById(R.id.conference_role);
        mRoleSpinner = (Spinner) findViewById(R.id.role_spinner);
        mChairPassword = (EditText) findViewById(R.id.chair_password);
        mButton = (Button) findViewById(R.id.choose_role_button);
        
        mRoleSpinner.setOnItemSelectedListener(this);
        mButton.setOnClickListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected Feature getLocationFeature(Location location)
			throws EnvSocialContentException {
		Feature conferenceRoleFeature = location.getFeature(Feature.CONFERENCE_ROLE);
		if (conferenceRoleFeature == null) {
			EnvSocialResource locationResource = 
					location.isEnvironment() ? EnvSocialResource.ENVIRONMENT : EnvSocialResource.AREA;
			throw new EnvSocialContentException(location.serialize(), locationResource, null);
		}
		return conferenceRoleFeature;
	}

	@Override
	protected void onFeatureDataInitialized(Feature newFeature, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFeatureDataUpdated(Feature updatedFeature, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getActiveUpdateDialogMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Resources res = getResources();
		String[] roles = res.getStringArray(R.array.role_array);
		if (parent.getItemAtPosition(pos).toString().equals(roles[2])) {
			mChairPassword.setVisibility(View.VISIBLE);
		} else {
			mChairPassword.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
