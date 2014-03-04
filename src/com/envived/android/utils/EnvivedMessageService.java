package com.envived.android.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class EnvivedMessageService extends IntentService {
	private static final String TAG = "EnvivedMessageService";
	public static final String LONG_POLL_URL = "http://192.168.1.192:8080/envived/client/notifications/me/";
	public static final int MSG_TIMEOUT_MILLIS = 60000;
	private boolean stopFlag = false;
	
	private HttpGet mGetRequest;
	public static final String UPDATE_NOTIFICATION = "com.envived.android.permission.UPDATE_NOTIFICATION";
	public final static String ACTION_RECEIVE_UPDATE_NOTIFICATION = "com.envived.android.intent.RECEIVE_UPDATE_NOTIFICATION";
	public final static String ACTION_RECEIVE_MESSAGE_NOTIFICATION = "com.envived.android.intent.RECEIVE_MESSAGE_NOTIFICATION";
	public final static String ACTION_RECEIVE_EVENT_NOTIFICATION = "com.envived.android.intent.RECEIVE_EVENT_NOTIFICATION";

	public EnvivedMessageService() {
		super("EnvivedMessageService");
	}

	@Override
	protected void onHandleIntent(Intent arg) {
		DefaultHttpClient retrieveMsgClient = new DefaultHttpClient();
	    HttpParams httpParameters = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(httpParameters, MSG_TIMEOUT_MILLIS);
	    retrieveMsgClient.setParams(httpParameters);
	    mGetRequest = new HttpGet(LONG_POLL_URL);
	    HttpResponse response;
	    while(!stopFlag) {
			try {
				mGetRequest = new HttpGet(LONG_POLL_URL);
				
				String sessionCookie = Preferences.getStringPreference(EnvivedMessageService.this, com.envived.android.api.AppClient.SESSIONID);
				if (sessionCookie != null) {
					mGetRequest.setHeader("Cookie", sessionCookie);
				}
				response = retrieveMsgClient.execute(mGetRequest);
				
				ResponseHolder holder = ResponseHolder.parseResponse(response);
				JSONArray messages = holder.getJsonContent().getJSONObject("data").getJSONArray("messages");
				
				for (int i = 0; i < messages.length(); i++) {
					JSONObject jsonMsg = new JSONObject(messages.getString(i));
		        	String msgData = jsonMsg.getString("data");
		        	
		        	JSONObject msgJSON = new JSONObject(msgData);
		        	JSONObject contentJSON = msgJSON.getJSONObject("content");
	        		
	        		Intent broadcastIntent;
	        		
	        		if (msgJSON.getString("type").equals("envived_app_update")) {
	        			Log.d(TAG, "Update notification received");
	        			broadcastIntent = new Intent(ACTION_RECEIVE_UPDATE_NOTIFICATION);
	        		}
	        		else if (msgJSON.getString("type").equals("envived_app_message")) {
	        			Log.d(TAG, "Message notification received");
	        			broadcastIntent = new Intent(ACTION_RECEIVE_MESSAGE_NOTIFICATION);
	        		}
	        		else {
	        			Log.d(TAG, "Event notification received");
	        			broadcastIntent = new Intent(ACTION_RECEIVE_EVENT_NOTIFICATION);
	        		}

	        		broadcastIntent.putExtra("location_uri", contentJSON.getString("location_uri"));
	        		broadcastIntent.putExtra("resource_uri", contentJSON.getString("resource_uri"));
	        		broadcastIntent.putExtra("feature", contentJSON.getString("feature"));
	        		broadcastIntent.putExtra("params", contentJSON.getString("params"));
	        		
	        		sendBroadcast(broadcastIntent);
				}
			
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		stopFlag = true;
		
		Log.d(TAG, "onDestroy din service");
	}

}
