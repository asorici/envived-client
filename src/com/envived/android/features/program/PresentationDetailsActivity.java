package com.envived.android.features.program;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.envived.android.Envived;
import com.envived.android.HomeActivity;
import com.envived.android.R;
import com.envived.android.api.Location;
import com.envived.android.api.exceptions.EnvSocialContentException;
import com.envived.android.utils.AgentMock;
import com.envived.android.utils.AgentMock.LocalBinder;
import com.envived.android.utils.Preferences;
import com.envived.android.utils.ResponseHolder;
import com.envived.android.utils.Utils;
import com.envived.android.utils.imagemanager.ImageCache;
import com.envived.android.utils.imagemanager.ImageFetcher;

public class PresentationDetailsActivity extends SherlockFragmentActivity {
	private static final String TAG = "PresentationDetailsActivity";
	private static final String TITLE_TAG = "Presentation Details";
	
	private Location mLocation;
	private ProgramFeature mProgramFeature;
	private ImageFetcher mImageFetcher;
	
	private int mPresentationId;
	private String mLocationUrl;
	private String mLocationName;
	private String mTitle;
	private String mTags;
	private String mAbstract;
	private String mSessionTitle;
	private String mStartTime;
	private String mEndTime;
	private String startHour;
	private String endHour;
	private LinkedList<PresentationSpeakerInfo> mSpeakerInfoList;
	
	private TextView mTitleView;
	private TextView mDatetimeView;
	private TextView mSessionView;
	private TextView mLocationNameView;
	private TextView mTagsView;
	private WebView mAbstractView;
	private CheckBox mAttendCheckbox;
	
	private EditText mStartTimeEdit;
	private EditText mEndTimeEdit;
	private Button mSaveTimeButton;
	
	private LinearLayout mSpeakersLayout;
	private LinearLayout mScheduleLayout;
	
	private AgentMock mService;
    private boolean mBound = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(TITLE_TAG);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		// get intent parameters
		mLocation = (Location) getIntent().getExtras().getSerializable("location");
		mProgramFeature = (ProgramFeature)getIntent().getExtras().getSerializable("program_feature");
		mPresentationId = getIntent().getExtras().getInt(ProgramFeature.PRESENTATION_ID);
		
		
		// initialize feature
		try {
			Log.d(TAG, "initializing");
			mProgramFeature.init();
		} catch (EnvSocialContentException e) {
			Log.d(TAG, "Error initializing program feature.", e);
		}
		
		setContentView(R.layout.program_presentation_details);
		
		mTitleView = (TextView) findViewById(R.id.title);
		mSessionView = (TextView) findViewById(R.id.session);
		mLocationNameView = (TextView) findViewById(R.id.locationName);
		mDatetimeView = (TextView) findViewById(R.id.datetime);
		mTagsView = (TextView) findViewById(R.id.tags);
		mAbstractView = (WebView) findViewById(R.id.presentation_abstract);
		mAbstractView.getSettings().setBuiltInZoomControls(true);
		mAttendCheckbox = (CheckBox) findViewById(R.id.attendCheckbox);
		mAttendCheckbox.setOnClickListener(new AttendCheckBoxClickListener(mProgramFeature, mPresentationId));
		
		mStartTimeEdit = (EditText) findViewById(R.id.startingTime);
		mEndTimeEdit = (EditText) findViewById(R.id.endingTime);
		mSaveTimeButton = (Button) findViewById(R.id.saveTimeButton);
		
		mScheduleLayout = (LinearLayout) findViewById(R.id.scheduleLayout);
		
		mSaveTimeButton.setOnClickListener(new SaveTimeButtonClickListener());
		
		mSpeakersLayout = (LinearLayout) findViewById(R.id.presentation_speakers_layout);
		
		
		if (mProgramFeature != null && mPresentationId != -1) {
			// initialize url image fetcher
			initImageFetcher();
			
			Cursor presentationDetailsCursor = mProgramFeature.getPresentationDetails(mPresentationId);
			Cursor speakerInfoCursor = mProgramFeature.getPresentationSpeakerInfo(mPresentationId);
			bindData(presentationDetailsCursor, speakerInfoCursor);
		}
		
		String role = Preferences.getUserConferenceRole(getApplicationContext());
		if (role != null && role.equals("session chair")) {
			mScheduleLayout.setVisibility(View.VISIBLE);
		} else {
			mScheduleLayout.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		
		mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		mImageFetcher.setExitTasksEarly(false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	
		// check if due to delayed onDestroy (can happen from Notification relaunch) 
		// the image fetcher has closed the cache after it was opened again
		if (mImageFetcher == null || !mImageFetcher.cashOpen()) {
			initImageFetcher();
		}
		
		Intent intent = new Intent(this, AgentMock.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
		
		// close image fetcher cache
		mImageFetcher.closeCache();
		
		// cleanup the program feature
		mProgramFeature.doClose(getApplicationContext());
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.presentation_details_menu, menu);
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, HomeActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.view_presentation_comments:
	        	// start the presentation comments activity
	        	Intent i = new Intent(this, PresentationCommentsActivity.class);
	        	i.putExtra("presentation_id", mPresentationId);
	        	i.putExtra("presentation_title", mTitle);
	        	i.putExtra("location", mLocation);
	        	startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	private View getSeparatorView() {
		// instantiate speaker separator view
		View v = getLayoutInflater().inflate(R.layout.envived_layout_separator_default, mSpeakersLayout, false);
		return v.findViewById(R.id.layout_separator);
	}
	
	private void bindData(Cursor presentationDetailsCursor, Cursor speakerInfoCursor) {
		if (presentationDetailsCursor != null) {
			
			int idIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_ID);
			int titleIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_TITLE);
			int tagsIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_TAGS);
			int startTimeIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_START_TIME);
			int endTimeIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_END_TIME);
			int abstractIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_ABSTRACT);
			int markedIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_PRESENTATION_MARKED);
			
			int sessionTitleIndex = presentationDetailsCursor.getColumnIndex(ProgramFeature.SESSION);
			int locationUrlIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_SESSION_LOCATION_URL);
			int locationNameIndex = presentationDetailsCursor.getColumnIndex(ProgramDbHelper.COL_SESSION_LOCATION_NAME);
			
			
			// move cursor to first and only entry
			presentationDetailsCursor.moveToFirst();
			
			mPresentationId = presentationDetailsCursor.getInt(idIndex);
			mLocationUrl = presentationDetailsCursor.getString(locationUrlIndex);
			mLocationName = presentationDetailsCursor.getString(locationNameIndex);
			mTitle = presentationDetailsCursor.getString(titleIndex);
			mSessionTitle = presentationDetailsCursor.getString(sessionTitleIndex);
			mStartTime = presentationDetailsCursor.getString(startTimeIndex);
			mEndTime = presentationDetailsCursor.getString(endTimeIndex);
			mAttendCheckbox.setChecked(presentationDetailsCursor.getInt(markedIndex) == 1 ? true : false);
			
			if (!presentationDetailsCursor.isNull(tagsIndex)) {
				mTags = presentationDetailsCursor.getString(tagsIndex);
			}
			else {
				mTags = null;
			}
			
			if (!presentationDetailsCursor.isNull(abstractIndex)) {
				mAbstract = presentationDetailsCursor.getString(abstractIndex);
			}
			else {
				mAbstract = null;
			}
			
			// ======================= binding ========================
			mTitleView.setText(mTitle);
			
			Calendar startDate = null, endDate = null;
			try {
				startDate = Utils.stringToCalendar(mStartTime, "yyyy-MM-dd'T'HH:mm:ss");
				endDate = Utils.stringToCalendar(mEndTime, "yyyy-MM-dd'T'HH:mm:ss");
			} catch (ParseException e) {
				Log.d(TAG, "Error parsing presentation start time string: " + mStartTime);
			}
			
			if (startDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		        startHour = sdf.format(startDate.getTime());
		        endHour = sdf.format(endDate.getTime());
		        
		        sdf.applyPattern("dd MMM yyyy");
		        String startDay = sdf.format(startDate.getTime());
				mDatetimeView.setText(startHour + ",  " + startDay);
				
				mStartTimeEdit.setText(startHour);
				mEndTimeEdit.setText(endHour);
			}
			else {
				mDatetimeView.setText("Unknown");
			}
			
			mSessionView.setText(mSessionTitle);
			mLocationNameView.setText(mLocationName);
			
			if (mTags != null) {
				mTagsView.setText(mTags.replace(";", ", "));
			}
			
			if (mAbstract != null) {
				try {
					mAbstractView.loadData(URLEncoder.encode(mAbstract, "UTF-8").replaceAll("\\+", " "), "text/html", Encoding.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					Log.d(TAG, "ERROR loading presentation abstract in WebView.", e);
				}
			}
			else {
				String message = "No abstract available";
				mAbstractView.loadData(message, "text/html", Encoding.UTF_8.toString());
			}
		}
		
		if (speakerInfoCursor != null) {
			// consume speaker info cursor
			mSpeakerInfoList = new LinkedList<PresentationDetailsActivity.PresentationSpeakerInfo>();
			int speakerIdIndex = speakerInfoCursor.getColumnIndex(ProgramDbHelper.COL_SPEAKER_ID);
			int speakerFirstNameIndex = speakerInfoCursor.getColumnIndex(ProgramDbHelper.COL_SPEAKER_FIRST_NAME);
			int speakerLastNameIndex = speakerInfoCursor.getColumnIndex(ProgramDbHelper.COL_SPEAKER_LAST_NAME);
			int speakerImageUrlIndex = speakerInfoCursor.getColumnIndex(ProgramDbHelper.COL_SPEAKER_IMAGE_URL);
			
			while(speakerInfoCursor.moveToNext()) {
				int speakerId = speakerInfoCursor.getInt(speakerIdIndex);
				String speakerFirstName = speakerInfoCursor.getString(speakerFirstNameIndex);
				String speakerLastName = speakerInfoCursor.getString(speakerLastNameIndex);
				String speakerImageUrl = null;
				
				if (!speakerInfoCursor.isNull(speakerImageUrlIndex)) {
					speakerImageUrl = speakerInfoCursor.getString(speakerImageUrlIndex);
				}
				
				mSpeakerInfoList.add(
						new PresentationSpeakerInfo(speakerId, speakerImageUrl, speakerFirstName, speakerLastName));
			}
			
			setupSpeakerViews();
		}
	}
	
	private void setupSpeakerViews() {
		
		int len = mSpeakerInfoList.size();
		for (int i = 0; i < len; i++) {
			PresentationSpeakerInfo speakerInfo = mSpeakerInfoList.get(i); 
					
			View speakerInfoRowView = getLayoutInflater().
					inflate(R.layout.program_presentation_details_speaker_row, mSpeakersLayout, false);
			
			ImageView speakerImageView = (ImageView) speakerInfoRowView.findViewById(R.id.presentation_details_speaker_row_image);
			TextView speakerImageName = (TextView) speakerInfoRowView.findViewById(R.id.presentation_details_speaker_row_name);
		
			if (mImageFetcher != null && speakerInfo.getImageUrl() != null) {
				mImageFetcher.loadImage(speakerInfo.getImageUrl(), speakerImageView);
			}
			
			speakerImageName.setText(speakerInfo.getFirstName() + " " + speakerInfo.getLastName());
			speakerImageName.setOnClickListener(new PresentationSpeakerClickListener(speakerInfo.getSpeakerId()));
		
			// add speaker row view speaker layout
			mSpeakersLayout.addView(speakerInfoRowView);
			
			if (i != len - 1) {
				mSpeakersLayout.addView(getSeparatorView());
			}
		}
	}
	
	
	private void initImageFetcher() {
		ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getApplicationContext(), ImageCache.IMAGE_CACHE_DIR);
        cacheParams.memoryCacheEnabled = false;
        
        // The ImageFetcher takes care of loading images into ImageViews asynchronously
        mImageFetcher = Envived.getImageFetcherInstance(getSupportFragmentManager(), 
        		cacheParams, R.drawable.placeholder_medium);
	}
	
	
	private class PresentationSpeakerClickListener implements OnClickListener {
		private int mSpeakerId;
		
		PresentationSpeakerClickListener(int speakerId) {
			mSpeakerId = speakerId;
		}
		
		@Override
		public void onClick(View v) {
			//Log.d(TAG, "Launching speaker details activity for speakerId: " + mSpeakerId);
			Intent i = new Intent(PresentationDetailsActivity.this, SpeakerDetailsActivity.class);
			Bundle extras = new Bundle();
			extras.putInt(ProgramFeature.SPEAKER_ID, mSpeakerId);
			extras.putSerializable("program_feature", mProgramFeature);
			
			i.putExtras(extras);
			startActivity(i);
		}
	}
	
	private class SaveTimeButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			PutDataTask task = new PutDataTask();
			task.execute();
		}
	}
	
	private class AttendCheckBoxClickListener implements OnClickListener {

		ProgramFeature mProgramFeature;
		int mPresentationId;
		public AttendCheckBoxClickListener(ProgramFeature mProgramFeature, int mPresentationId) {
			this.mProgramFeature = mProgramFeature;
			this.mPresentationId = mPresentationId;
		}
		
		@Override
		public void onClick(View v) {
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		    try {
				cal.setTime(sdf.parse(mStartTime.split("T")[1]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			CheckBox c = (CheckBox) v;
			if (c.isChecked()) {
				mProgramFeature.setPresentationMarked(mPresentationId, true);
				mService.addAlarm(cal, mTitle);
			} else {
				mProgramFeature.setPresentationMarked(mPresentationId, false);
				mService.deleteAlarm(cal);
			}
		}
		
	}
	
	
	static class PresentationSpeakerInfo implements Serializable {
		private static final long serialVersionUID = 1L;

		int mSpeakerId;

		String mImageUrl;
		String mFirstName;
		String mLastName;
		
		public PresentationSpeakerInfo(int speakerId, String imageUrl, String firstName, String lastName) {
			mSpeakerId = speakerId;
			mImageUrl = imageUrl;
			mFirstName = firstName;
			mLastName = lastName;
		}
		
		public int getSpeakerId() {
			return mSpeakerId;
		}


		public String getImageUrl() {
			return mImageUrl;
		}

		public String getFirstName() {
			return mFirstName;
		}

		public String getLastName() {
			return mLastName;
		}
	}
	
	private class PutDataTask extends  AsyncTask<Void, Void, ResponseHolder> {
		private String role = "";
		
		@Override
		protected ResponseHolder doInBackground(Void... params) {
			String oldStartTime, newStartTime;
			String oldEndTime, newEndTime;
			
			Calendar startDate = null, endDate = null;
			
			try {
				startDate = Utils.stringToCalendar(mStartTime, "yyyy-MM-dd'T'HH:mm:ss");
				endDate = Utils.stringToCalendar(mEndTime, "yyyy-MM-dd'T'HH:mm:ss");
			} catch (ParseException e) {
				Log.d(TAG, "Error parsing presentation start time string: " + mStartTime);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
	        oldStartTime = sdf.format(startDate.getTime());
	        oldEndTime = sdf.format(endDate.getTime());
	        
	        sdf = new SimpleDateFormat("yyyy-MM-dd");
	        
	        newStartTime = sdf.format(startDate.getTime()) + " " + mStartTimeEdit.getText().toString() + ":00";
	        newEndTime = sdf.format(endDate.getTime()) + " " + mEndTimeEdit.getText().toString() + ":00";
	        
	        String message = "{\"old_start_time\": \"" + oldStartTime + "\", \"new_start_time\": \"" + 
	        				newStartTime + "\", \"old_end_time\": \"" +
	        		        oldEndTime + "\", \"new_end_time\": \"" + newEndTime + "\"}";
	        
	        ResponseHolder holder = mProgramFeature.putToServer(getApplicationContext(), "program", mProgramFeature.getResourceUri(), false, message);
			
	        Log.d(TAG, holder.getCode() + " " + holder.getError() + " " + holder.getResponseBody());
	        
			return holder;
		}
		
		@Override
		protected void onPostExecute(ResponseHolder holder) {
			if (holder != null && !holder.hasError()) {
				if (holder.getCode() == 204) {
					Toast t = Toast.makeText(getApplicationContext(), "Schedule Updated", Toast.LENGTH_LONG);
					t.show();
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
	
	private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
	
}
