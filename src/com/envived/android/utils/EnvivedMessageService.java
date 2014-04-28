package com.envived.android.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

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
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.envived.android.api.Url;
import com.envived.android.api.agent.AgentBridge;
import com.google.gson.Gson;

/**
 * EnvivedMessageService runs as a service in the background, not visible to the user. It implements the long polling mechanism
 * by polling the server for minutes at a time. If no message is received, then it checks if the service was supposed to stop,
 * and if not it polls again. In case a message is received from the server, it checks if the message type of the message and
 * send the content by broadcast to the receivers. Only the receivers which work with that type of message will get it.
 * The types of messages are: envived_app_update, envived_app_message and envived_event. 
 * 
 * @author alextomescu
 *
 */
public class EnvivedMessageService extends IntentService {
	private static final String TAG = "EnvivedMessageService";
	private static final String URL_BASE = Url.HTTP + Url.HOSTNAME;
	public static final String LONG_POLL_URL = URL_BASE + "/envived/client/notifications/me/";
	public static final int MSG_TIMEOUT_MILLIS = 300000;
	private boolean stopFlag = false;
	Context context;
	
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
		// set up the connection to the server
		DefaultHttpClient retrieveMsgClient = new DefaultHttpClient();
	    HttpParams httpParameters = new BasicHttpParams();
	    HttpConnectionParams.setSoTimeout(httpParameters, MSG_TIMEOUT_MILLIS);
	    retrieveMsgClient.setParams(httpParameters);
	    mGetRequest = new HttpGet(LONG_POLL_URL);
	    HttpResponse response;
	    
	    // while the service is not stopped keep polling the server
	    while(!stopFlag) {
			try {
				mGetRequest = new HttpGet(LONG_POLL_URL);
				
				// retrieve a session id from the program and add it to the header of the request for authentication.
				String sessionCookie = Preferences.getStringPreference(EnvivedMessageService.this, com.envived.android.api.AppClient.SESSIONID);
				if (sessionCookie != null) {
					mGetRequest.setHeader("Cookie", sessionCookie);
				}
				response = retrieveMsgClient.execute(mGetRequest);
				
				ResponseHolder holder = ResponseHolder.parseResponse(response);
				Log.d(TAG, "NOTIFICATION_MESSAGE: " + holder.getResponseBody());
				
				JSONArray messages = holder.getJsonContent().getJSONObject("data").getJSONArray("messages");
				
				// extract the content from all the messages and send it to the appropriate receiver.
				for (int i = 0; i < messages.length(); i++) {
					JSONObject jsonMsg = new JSONObject(messages.getString(i));
		        	String msgData = jsonMsg.getString("data");
		        	JSONObject msgJson = new JSONObject(msgData);
		        	Intent intent = null;
		        	
		        	Gson gson = new Gson();
		        	if (msgJson.getString("type").equals("envived_app_update")) {
		        		EnvivedAppUpdate appUpdate = gson.fromJson(msgJson.getString("content"), EnvivedAppUpdate.class);
		        		intent = new Intent(ACTION_RECEIVE_UPDATE_NOTIFICATION);
		        		intent.putExtra("envived_app_update", appUpdate);
		        		//Log.d(TAG, appUpdate.toString());
		        	} else if (msgJson.getString("type").equals("envived_event")) {
		        		EnvivedEvent event = gson.fromJson(msgJson.getString("content"), EnvivedEvent.class);
		        		intent = new Intent(this, AgentBridge.class);
	        			intent.putExtra("envived_event", event);
	        			startService(intent);
	        			//Log.d(TAG, event.toString());
	        			continue;
		        	} else {
		        		// TODO
		        	}
	        		
	        		if (!stopFlag) {
	        			sendBroadcast(intent);
	        		}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				Log.d(TAG, "Message service long polling timeout");
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
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
