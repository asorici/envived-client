package com.envived.android.features.conferencerole;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.envived.android.R;
import com.envived.android.api.EnvSocialResource;
import com.envived.android.api.Location;
import com.envived.android.api.exceptions.EnvSocialContentException;
import com.envived.android.features.EnvivedFeatureActivity;
import com.envived.android.features.Feature;
import com.envived.android.utils.Preferences;
import com.envived.android.utils.ResponseHolder;

public class ConferenceRoleActivity extends EnvivedFeatureActivity implements OnItemSelectedListener, OnClickListener {
	private static final String TAG = "ConferenceRoleActivity";
	
	private static final String PARTICIPANT_ROLE = "participant";
	private static final String SPEAKER_ROLE = "speaker";
	private static final String CHAIR_ROLE = "session_chair";
	
	private Spinner mRoleSpinner;
	private EditText mChairPassword;
	private Button mButton;
	
	private ConferenceRoleFeature mConferenceFeature;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        setContentView(R.layout.conference_role);
        
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
		Log.d(TAG, conferenceRoleFeature.toString());
		if (conferenceRoleFeature == null) {
			EnvSocialResource locationResource = 
					location.isEnvironment() ? EnvSocialResource.ENVIRONMENT : EnvSocialResource.AREA;
			throw new EnvSocialContentException(location.serialize(), locationResource, null);
		}
		return conferenceRoleFeature;
	}
	
	private void bindData() {
		String role = mConferenceFeature.getRole();
		if (role.equals(PARTICIPANT_ROLE)) {
			this.mRoleSpinner.setSelection(0);
		} else if (role.equals(SPEAKER_ROLE)) {
			this.mRoleSpinner.setSelection(1);
		} else {
			this.mRoleSpinner.setSelection(2);
		}
	}

	@Override
	protected void onFeatureDataInitialized(Feature newFeature, boolean success) {
		Log.d(TAG, success + "AAAAA");
		if (success) {
			mConferenceFeature = (ConferenceRoleFeature) newFeature;
			bindData();
		}
	}

	@Override
	protected void onFeatureDataUpdated(Feature updatedFeature, boolean success) {
		if (success) {
			mConferenceFeature = (ConferenceRoleFeature) updatedFeature;
			bindData();
		}
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
		PutDataTask putTask = new PutDataTask();
		putTask.execute(mLocation);
	}
	
	private class PutDataTask extends  AsyncTask<Location, Void, ResponseHolder> {
		private String role = "";
		
		@Override
		protected ResponseHolder doInBackground(Location... arg0) {
			String data = "";
			if (mRoleSpinner.getSelectedItemId() == 2) {
				data = "{\"role\":\"" + CHAIR_ROLE + "\", \"chair_password\":\""
						+ mChairPassword.getText().toString() + "\"}";
			} else {
				data = "{\"role\":\"" + mRoleSpinner.getSelectedItem().toString().toLowerCase() + "\"}";
			}
			
			role = mRoleSpinner.getSelectedItem().toString().toLowerCase();
			Log.d(TAG, mConferenceFeature.toString());
			ResponseHolder holder = mConferenceFeature.putToServer(getApplicationContext(),
					"conference_role", mConferenceFeature.getResourceUri(),
					false, data);
			return holder;
		}
		
		@Override
		protected void onPostExecute(ResponseHolder holder) {
			if (holder != null && !holder.hasError()) {
				if (holder.getCode() == 204) {
					Toast t = Toast.makeText(getApplicationContext(), "Role changed!", Toast.LENGTH_SHORT);
					t.show();
					Preferences.setUserConferenceRole(getApplicationContext(), role);
					finish();
				} else if (holder.getCode() == 401) {
					Toast t = Toast.makeText(getApplicationContext(), "Unauthorized", Toast.LENGTH_LONG);
					t.show();
				}
			}
			else {
				//Log.d(TAG, holder.getResponseBody(), holder.getError());
			}
		}
		
	}

}
