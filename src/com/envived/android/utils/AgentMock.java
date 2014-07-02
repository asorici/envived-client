package com.envived.android.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AgentMock extends Service {
	TreeMap<Calendar, String> presentations;
    private final IBinder mBinder = new LocalBinder();
    AlarmManager alarmManager;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("AgentMock", "Received start id " + startId + ": " + intent);
	    return START_STICKY;
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onCreate() {
		presentations = new TreeMap<Calendar, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()+ 10*1000));
		presentations.put(cal, "test");
		Intent notificationServiceIntent = new Intent(AgentMock.this , NotificationService.class);     
		notificationServiceIntent.putExtra("title", "test");
	    alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    PendingIntent pendingIntent = PendingIntent.getService(AgentMock.this, 0, notificationServiceIntent, 0);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, presentations.firstEntry().getKey().getTimeInMillis(), pendingIntent);
    }
	
	public class LocalBinder extends Binder {
		public AgentMock getService() {
            return AgentMock.this;
        }
    }
	
	@Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

	public void addAlarms(TreeMap<Calendar, String> alarms) {
		presentations.putAll(alarms);
		updateAlarms();
	}
	
	public void addAlarm(Calendar time, String title) {
		Log.d("AgentMock", time.toString() + " " + title);
		presentations.put(time,  title);
		updateAlarms();
	}
	
	public void deleteAlarm(Calendar time) {
		presentations.remove(time);
	}
	
	private void updateAlarms() {
		int i = 0;
		for (Entry<Calendar, String> entry : presentations.entrySet()) {
			Intent intent = new Intent(AgentMock.this, NotificationService.class);
			intent.putExtra("title", entry.getValue());
			PendingIntent pendingIntent = PendingIntent.getService(AgentMock.this, i++, intent, 0);
			alarmManager.set(AlarmManager.RTC_WAKEUP, entry.getKey().getTimeInMillis(), pendingIntent);
		}
	}
}
