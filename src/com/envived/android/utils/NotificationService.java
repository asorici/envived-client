package com.envived.android.utils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.envived.android.R;

public class NotificationService extends IntentService {
	public NotificationService() {
		super("NotificationService");
	}

	NotificationManager nm;
	int mId = 0;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("NotificationService", "Received start id " + startId + ": " + intent);
		String title = intent.getStringExtra("title");
		nm = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.icon)
		        .setContentTitle("The " + title + " presentation is starting!");
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(mId, mBuilder.build());
		
	    return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("NotificationService", "BLABLA");
		
	}

}
