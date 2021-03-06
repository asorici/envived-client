package com.envived.android.features.order;

import android.content.Context;
import android.content.Intent;

import com.envived.android.R;
import com.envived.android.utils.EnvivedAppUpdate;
import com.envived.android.utils.FeatureUpdateNotification;

public class OrderFeatureUpdateNotification extends FeatureUpdateNotification {
	private int mId;
	private int mIconId;
	private String mTitle;
	private long mWhen;
	private String mMessage;
	
	public OrderFeatureUpdateNotification(Context context, Intent intent,
			EnvivedAppUpdate appUpdate) {
		super(context, intent, appUpdate);
		
		mId = R.string.update_order_feature;
		mIconId = R.drawable.ic_launcher;
		mTitle = mContext.getResources().getString(R.string.update_order_feature);
		mWhen = System.currentTimeMillis();
			
		mMessage = "Your menu has been updated!";
	}
	
	
	@Override
	public int getNotificationId() {
		return mId;
	}

	@Override
	public int getNotificationIcon() {
		return mIconId;
	}

	@Override
	public String getNotificationTitle() {
		return mTitle;
	}

	@Override
	public long getNotificationWhen() {
		return mWhen;
	}

	@Override
	public String getNotificationMessage() {
		return mMessage;
	}
}
