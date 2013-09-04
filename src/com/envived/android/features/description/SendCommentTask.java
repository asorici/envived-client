package com.envived.android.features.description;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.envived.android.R;
import com.envived.android.R.string;
import com.envived.android.R.style;
import com.envived.android.api.Annotation;
import com.envived.android.api.exceptions.EnvivedComException;
import com.envived.android.api.exceptions.EnvivedContentException;
import com.envived.android.utils.ResponseHolder;

public class SendCommentTask extends AsyncTask<Void, Void, ResponseHolder> {
	private static final String TAG = "SendCommentTask";
	
	// loader dialog for sending the comment
	private ProgressDialog mSendCommentDialog;
	private Context mContext;
	private boolean error = true;
	
	private String mCommentSubject;
	private Annotation mCommentRequest;
	
	private CommentsActivity mCommentsActivity;
	
	public SendCommentTask(Context context, CommentsActivity commentsActivity, Annotation commentRequest) {
		mContext = context;
		mCommentRequest = commentRequest;
		mCommentsActivity = commentsActivity;
	}
	
	@Override
	protected void onPreExecute() {
		mSendCommentDialog = new ProgressDialog(new ContextThemeWrapper(mContext, R.style.ProgressDialogWhiteText));
		mSendCommentDialog.setMessage("Sending comment ...");
		mSendCommentDialog.setIndeterminate(true);
		mSendCommentDialog.setCanceledOnTouchOutside(true);
		
		mSendCommentDialog.show();
	}
	
	@Override
	protected ResponseHolder doInBackground(Void...args) {
		return mCommentRequest.post(mContext);
	}
	
	@Override
	protected void onPostExecute(ResponseHolder holder) {
		mSendCommentDialog.cancel();
		
		if (!holder.hasError()) {
			error = false;
			int msgId = R.string.msg_send_comment_ok;
			
			switch(holder.getCode()) {
			case HttpStatus.SC_CREATED:
				error = false;
				break;
			default:
				msgId = R.string.msg_send_comment_err;
				error = true;
				break;
			}
			
			if (error) {
				Log.d(TAG, "response code: " + holder.getCode() + " response body: " + holder.getResponseBody());
				Toast toast = Toast.makeText( mContext,
						msgId, Toast.LENGTH_LONG);
				toast.show();
			}
			else {
				Toast toast = Toast.makeText( mContext,
						msgId, Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			int msgId = R.string.msg_service_unavailable;

			try {
				throw holder.getError();
			} catch (EnvivedComException e) {
				Log.d(TAG, e.getMessage(), e);
				msgId = R.string.msg_service_unavailable;
			} catch (EnvivedContentException e) {
				Log.d(TAG, e.getMessage(), e);
				msgId = R.string.msg_service_error;
			} catch (Exception e) {
				Log.d(TAG, e.toString(), e);
				msgId = R.string.msg_service_error;
			}

			Toast toast = Toast.makeText(mContext, msgId, Toast.LENGTH_LONG);
			toast.show();
		}
		
		mCommentsActivity.postSendComment();
	}
}