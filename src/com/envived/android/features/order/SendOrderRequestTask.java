package com.envived.android.features.order;

import org.apache.http.HttpStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.envived.android.HomeActivity;
import com.envived.android.R;
import com.envived.android.api.Annotation;
import com.envived.android.api.exceptions.EnvSocialComException;
import com.envived.android.api.exceptions.EnvSocialContentException;
import com.envived.android.utils.ResponseHolder;

public class SendOrderRequestTask extends AsyncTask<Void, Void, ResponseHolder> {
	private static final String TAG = "SendOrderTask";
	
	// loader dialog for sending an order
	private ProgressDialog mSendOrderDialog;
	private Context mContext;
	private ISendOrderRequest mOrderFragment;
	private boolean error = true;
	
	private String mOrderRequestType;
	private Annotation mOrderRequest;
	
	
	public SendOrderRequestTask(Context context, ISendOrderRequest orderFragment, String orderRequestType,
			Annotation orderRequest) {
		mOrderRequestType = orderRequestType;
		mOrderRequest = orderRequest;
		mContext = context;
		mOrderFragment = orderFragment;
	}
	
	
	@Override
	protected void onPreExecute() {
		mSendOrderDialog = new ProgressDialog(new ContextThemeWrapper(mContext, R.style.ProgressDialogWhiteText));
		mSendOrderDialog.setMessage("Sending Request ...");
		mSendOrderDialog.setIndeterminate(true);
		mSendOrderDialog.setCanceledOnTouchOutside(true);
		
		mSendOrderDialog.show();
	}
	
	
	@Override
	protected ResponseHolder doInBackground(Void...args) {
		return mOrderRequest.post(mContext);
	}
	
	
	@Override
	protected void onPostExecute(ResponseHolder holder) {
		mSendOrderDialog.cancel();
		
		if (!holder.hasError()) {
			error = false;
			int msgId = R.string.msg_send_order_ok;

			switch(holder.getCode()) {
			case HttpStatus.SC_CREATED:
			case HttpStatus.SC_ACCEPTED:
			case HttpStatus.SC_NO_CONTENT:
				error = false;
				break;

			case HttpStatus.SC_BAD_REQUEST:
				msgId = R.string.msg_send_order_400;
				error = true;
				break;

			case HttpStatus.SC_UNAUTHORIZED:
				msgId = R.string.msg_send_order_401;
				error = true;
				break;

			case HttpStatus.SC_METHOD_NOT_ALLOWED:
				msgId = R.string.msg_send_order_405;
				error = true;
				break;

			default:
				msgId = R.string.msg_send_order_err;
				error = true;
				break;
			}

			if (error) {
				Log.d(TAG, "[DEBUG]>> Error sending order: " + mContext.getString(msgId) + " " + holder.getResponseBody());
				Toast toast = Toast.makeText( mContext,
						msgId, Toast.LENGTH_LONG);
				toast.show();
			}
			else {
				Toast toast = Toast.makeText( mContext,
						msgId, Toast.LENGTH_LONG);
				toast.show();
			}
		}
		else {
			int msgId = R.string.msg_service_unavailable;

			try {
				throw holder.getError();
			} catch (EnvSocialComException e) {
				Log.d(TAG, e.getMessage(), e);
				msgId = R.string.msg_service_unavailable;
			} catch (EnvSocialContentException e) {
				Log.d(TAG, e.getMessage(), e);
				msgId = R.string.msg_service_error;
			} catch (Exception e) {
				Log.d(TAG, e.toString(), e);
				msgId = R.string.msg_service_error;
			}

			Toast toast = Toast.makeText(mContext, msgId, Toast.LENGTH_LONG);
			toast.show();
		}
		
		// call post send order handler on the parent fragment
		mOrderFragment.postSendOrderRequest(mOrderRequestType, mOrderRequest, !error);
	}
	
	
	public boolean successful() {
		return !error;
	}
}