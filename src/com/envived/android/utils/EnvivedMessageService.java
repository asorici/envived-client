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

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class EnvivedMessageService extends IntentService {
	private static final String TAG = "EnvivedMessageService";
	public static final String LONG_POLL_URL = "http://192.168.1.192:8080/envived/client/notifications/me/";
	public static final int MSG_TIMEOUT_MILLIS = 60000;
	private boolean stopFlag = false;
	
	private HttpGet mGetRequest;
	public static final String NOTIFICATION = "com.envived.android.permission.NOTIFICATION";

	public EnvivedMessageService() {
		super("EnvivedMessageService");
	}

	@Override
	protected void onHandleIntent(Intent arg) {
		DefaultHttpClient retrieveMsgClient = new DefaultHttpClient();
	    HttpParams httpParameters = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(httpParameters, MSG_TIMEOUT_MILLIS);
	    Gson gson = new Gson();
	    retrieveMsgClient.setParams(httpParameters);
	    mGetRequest = new HttpGet(LONG_POLL_URL);
	    HttpResponse response;
	    while(!stopFlag) {
			try {
				Log.d(TAG, "test3");
				mGetRequest = new HttpGet(LONG_POLL_URL);
				
				String sessionCookie = Preferences.getStringPreference(EnvivedMessageService.this, com.envived.android.api.AppClient.SESSIONID);
				if (sessionCookie != null) {
					mGetRequest.setHeader("Cookie", sessionCookie);
				}
				response = retrieveMsgClient.execute(mGetRequest);
				
				Log.d(TAG, "test4");
				
				ResponseHolder holder = ResponseHolder.parseResponse(response);
				JSONObject messagesWrapper = new JSONObject(holder.getResponseBody()).getJSONObject("data");
				
				//EnvivedMessage message = gson.fromJson(messagesWrapper.toString(), EnvivedMessage.class);
				
				Log.d(TAG, messagesWrapper.toString());
				
				Intent intent = new Intent();
				intent.putExtra("type", "blargh");
				
				sendBroadcast(intent);
			
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	    
	    Log.d(TAG, "stopped");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		stopFlag = true;
		
		Log.d(TAG, "onDestroy din service");
	}

}
